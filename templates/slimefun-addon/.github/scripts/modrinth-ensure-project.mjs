// Se asegura de que el proyecto exista en Modrinth y devuelve su id.
//
// Si la variable MODRINTH_PROJECT_ID del repo ya apunta a un proyecto existente, no hace nada.
// Si no existe --o no esta definida-- crea el proyecto con los datos del repo y escribe el id en
// la salida del step para que el resto del workflow lo use.
//
// El token llega por variable de entorno desde el secreto de la organizacion: nunca se imprime
// ni se escribe en disco. Cualquier error se reporta sin incluir la cabecera de autorizacion.

import { appendFileSync, existsSync, readFileSync } from 'fs';

const API = 'https://api.modrinth.com/v2';
const TOKEN = process.env.MODRINTH_TOKEN;
const SLUG_DESEADO = (process.env.PROJECT_SLUG || '').toLowerCase();
const NOMBRE = process.env.PROJECT_NAME || SLUG_DESEADO;
const RESUMEN = (process.env.PROJECT_SUMMARY || `Addon de Slimefun para Paper 1.21`).slice(0, 256);

if (!TOKEN) {
  console.error('Falta MODRINTH_TOKEN.');
  process.exit(1);
}

const cabeceras = { Authorization: TOKEN, 'User-Agent': 'DrakesCraft-Labs/publicador' };

async function buscar(idOslug) {
  if (!idOslug) return null;
  const r = await fetch(`${API}/project/${encodeURIComponent(idOslug)}`, { headers: cabeceras });
  if (r.status === 200) return await r.json();
  return null;
}

// 1. ¿Ya existe?
let proyecto = (await buscar(process.env.MODRINTH_PROJECT_ID)) || (await buscar(SLUG_DESEADO));

// 2. Si no existe, se crea.
if (!proyecto) {
  console.log(`No existe el proyecto "${SLUG_DESEADO}"; se crea.`);

  const cuerpo = existsSync('README.md')
    ? readFileSync('README.md', 'utf8')
    : `# ${NOMBRE}\n\nAddon de Slimefun para DrakesCraft.`;

  const datos = {
    slug: SLUG_DESEADO,
    title: NOMBRE,
    description: RESUMEN,
    body: cuerpo,
    categories: ['utility'],
    client_side: 'unsupported',
    server_side: 'required',
    project_type: 'mod',
    is_draft: true, // se crea como borrador: nada se hace publico sin revision humana
    license_id: 'GPL-3.0-only',
  };

  const form = new FormData();
  form.append('data', JSON.stringify(datos));

  const r = await fetch(`${API}/project`, { method: 'POST', headers: cabeceras, body: form });
  if (!r.ok) {
    console.error(`No se pudo crear el proyecto (HTTP ${r.status}): ${(await r.text()).slice(0, 400)}`);
    process.exit(1);
  }
  proyecto = await r.json();
  console.log(`Proyecto creado como BORRADOR: ${proyecto.slug} (${proyecto.id})`);
  console.log('Revisalo y publicalo a mano en Modrinth cuando este listo.');
}

if (process.env.GITHUB_OUTPUT) {
  appendFileSync(process.env.GITHUB_OUTPUT, `project_id=${proyecto.id}\n`);
  appendFileSync(process.env.GITHUB_OUTPUT, `project_slug=${proyecto.slug}\n`);
}
console.log(`Proyecto en uso: ${proyecto.slug} (${proyecto.id})`);
