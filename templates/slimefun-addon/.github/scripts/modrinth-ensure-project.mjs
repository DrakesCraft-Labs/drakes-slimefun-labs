// Se asegura de que el proyecto exista en Modrinth, DENTRO DE LA ORGANIZACION, y devuelve su id.
//
// Modrinth crea todo proyecto bajo el usuario del token: no hay forma de crearlo directamente en
// una organizacion. El proyecto se crea y despues se traslada a la organizacion con el endpoint
// de organizaciones de la API v3. Sin ese segundo paso los addons acaban colgando del perfil
// personal en vez de la organizacion, que es justo lo que hay que evitar.
//
// La organizacion se toma de la variable MODRINTH_ORG del repo o de la organizacion del usuario
// del token si solo tiene una. El token llega por variable de entorno desde el secreto de la
// organizacion de GitHub: nunca se imprime ni se escribe en disco.

import { appendFileSync, existsSync, readFileSync } from 'fs';

const V2 = 'https://api.modrinth.com/v2';
const V3 = 'https://api.modrinth.com/v3';
const TOKEN = process.env.MODRINTH_TOKEN;
const SLUG = (process.env.PROJECT_SLUG || '').toLowerCase();
const NOMBRE = process.env.PROJECT_NAME || SLUG;
const RESUMEN = (process.env.PROJECT_SUMMARY || 'Addon de Slimefun4 para Paper 1.21.11.').slice(0, 256);
const ORG_PEDIDA = process.env.MODRINTH_ORG || '';

if (!TOKEN) {
  console.error('Falta MODRINTH_TOKEN.');
  process.exit(1);
}

const cabeceras = { Authorization: TOKEN, 'User-Agent': 'DrakesCraft-Labs/publicador' };

async function pedir(url, opciones = {}) {
  const r = await fetch(url, { ...opciones, headers: { ...cabeceras, ...(opciones.headers || {}) } });
  return r;
}

/** El proyecto si existe, o null. */
async function buscarProyecto(idOslug) {
  if (!idOslug) return null;
  const r = await pedir(`${V2}/project/${encodeURIComponent(idOslug)}`);
  return r.status === 200 ? await r.json() : null;
}

/** La organizacion donde deben vivir los proyectos, o null si no se puede determinar. */
async function resolverOrganizacion() {
  if (ORG_PEDIDA) {
    const r = await pedir(`${V3}/organization/${encodeURIComponent(ORG_PEDIDA)}`);
    if (r.status === 200) return await r.json();
    console.error(`La organizacion "${ORG_PEDIDA}" no existe o el token no la ve.`);
    return null;
  }
  // Sin variable: si el usuario del token pertenece a una sola organizacion, se usa esa.
  const usuario = await pedir(`${V3}/user`);
  if (usuario.status !== 200) return null;
  const yo = await usuario.json();
  const orgs = await pedir(`${V3}/user/${yo.id}/organizations`);
  if (orgs.status !== 200) return null;
  const lista = await orgs.json();
  if (Array.isArray(lista) && lista.length === 1) return lista[0];
  if (Array.isArray(lista) && lista.length > 1) {
    console.error(`El usuario pertenece a ${lista.length} organizaciones: define MODRINTH_ORG.`);
  }
  return null;
}

const organizacion = await resolverOrganizacion();
if (organizacion) {
  console.log(`Organizacion destino: ${organizacion.name || organizacion.slug} (${organizacion.id})`);
} else {
  console.log('Sin organizacion resuelta; el proyecto quedara bajo el usuario del token.');
}

let proyecto = (await buscarProyecto(process.env.MODRINTH_PROJECT_ID)) || (await buscarProyecto(SLUG));

if (!proyecto) {
  console.log(`No existe el proyecto "${SLUG}"; se crea.`);

  const cuerpo = existsSync('README.md')
    ? readFileSync('README.md', 'utf8')
    : `# ${NOMBRE}\n\nAddon de Slimefun para DrakesCraft.`;

  const datos = {
    slug: SLUG,
    title: NOMBRE,
    description: RESUMEN,
    body: cuerpo,
    categories: ['utility'],
    client_side: 'unsupported',
    server_side: 'required',
    project_type: 'mod',
    // MODRINTH_DRAFT=true deja el proyecto en borrador; por defecto se envia a la cola de
    // revision de Modrinth, que es el paso previo a que sea publico.
    is_draft: process.env.MODRINTH_DRAFT === 'true',
    license_id: process.env.PROJECT_LICENSE || 'GPL-3.0-only',
    // La API v2 lo exige aunque se cree vacio: el jar se sube despues como version
    // propia, no en la creacion del proyecto.
    initial_versions: [],
  };

  const form = new FormData();
  form.append('data', JSON.stringify(datos));

  const r = await pedir(`${V2}/project`, { method: 'POST', body: form });
  if (!r.ok) {
    console.error(`No se pudo crear el proyecto (HTTP ${r.status}): ${(await r.text()).slice(0, 400)}`);
    process.exit(1);
  }
  proyecto = await r.json();
  console.log(`Proyecto creado como BORRADOR: ${proyecto.slug} (${proyecto.id})`);
}

// Trasladarlo a la organizacion si aun no pertenece a ella.
if (organizacion && proyecto.organization !== organizacion.id) {
  const r = await pedir(`${V3}/organization/${organizacion.id}/projects`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ project_id: proyecto.id }),
  });
  if (r.ok) {
    console.log(`Proyecto trasladado a la organizacion ${organizacion.slug}.`);
  } else {
    console.error(`No se pudo trasladar a la organizacion (HTTP ${r.status}): ${(await r.text()).slice(0, 300)}`);
  }
}

if (process.env.GITHUB_OUTPUT) {
  appendFileSync(process.env.GITHUB_OUTPUT, `project_id=${proyecto.id}\n`);
  appendFileSync(process.env.GITHUB_OUTPUT, `project_slug=${proyecto.slug}\n`);
}
console.log(`Proyecto en uso: ${proyecto.slug} (${proyecto.id})`);
