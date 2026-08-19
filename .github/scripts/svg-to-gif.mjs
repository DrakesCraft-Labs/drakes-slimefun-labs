// Convierte un banner .svg ANIMADO en un .gif, preservando la animacion.
//
// Los banners de DrakesCraft usan animacion SVG (SMIL / @keyframes CSS), que ninguna herramienta
// de rasterizado estatica (rsvg-convert, resvg) sabe animar: sacarian un PNG de un solo frame.
// Aqui se abre el SVG en un Chromium sin cabeza, se capturan fotogramas a lo largo de un ciclo y
// se ensamblan en un GIF en bucle. Es lo que hace falta porque Modrinth no renderiza SVG en su
// galeria y un PNG perderia el movimiento.
//
// Uso: node svg-to-gif.mjs <entrada.svg> <salida.gif> [ancho] [alto] [fotogramas] [duracionMs]

import puppeteer from 'puppeteer';
import { readFileSync, mkdirSync, rmSync } from 'fs';
import { execSync } from 'child_process';

const [, , svgPath, outGif, w = '1280', h = '640', framesArg = '40', durArg = '4000'] = process.argv;

if (!svgPath || !outGif) {
  console.error('Uso: node svg-to-gif.mjs <entrada.svg> <salida.gif> [ancho] [alto] [fotogramas] [duracionMs]');
  process.exit(1);
}

const width = Number(w);
const height = Number(h);
const frames = Number(framesArg);
const durationMs = Number(durArg);
const stepMs = durationMs / frames;

const svg = readFileSync(svgPath, 'utf8');

const browser = await puppeteer.launch({ args: ['--no-sandbox', '--disable-setuid-sandbox'] });
try {
  const page = await browser.newPage();
  await page.setViewport({ width, height, deviceScaleFactor: 1 });
  // Fondo transparente para respetar el diseno del banner.
  await page.setContent(
    `<!doctype html><html><body style="margin:0;padding:0;background:transparent">${svg}</body></html>`,
    { waitUntil: 'networkidle0' }
  );

  const dir = '.frames';
  rmSync(dir, { recursive: true, force: true });
  mkdirSync(dir, { recursive: true });

  for (let i = 0; i < frames; i++) {
    await new Promise((r) => setTimeout(r, stepMs));
    await page.screenshot({
      path: `${dir}/f${String(i).padStart(3, '0')}.png`,
      omitBackground: true,
    });
  }

  // Delay de ImageMagick en centisegundos por fotograma; -loop 0 = bucle infinito.
  const delayCs = Math.max(2, Math.round(stepMs / 10));
  execSync(`convert -loop 0 -delay ${delayCs} ${dir}/f*.png -layers Optimize "${outGif}"`, {
    stdio: 'inherit',
  });
  rmSync(dir, { recursive: true, force: true });
  console.log(`GIF generado: ${outGif} (${frames} fotogramas, ${durationMs}ms)`);
} finally {
  await browser.close();
}
