// Convierte un icono .svg estatico en un .png de 512x512 para Modrinth.
//
// Modrinth rechaza iconos en formato SVG (HTTP 400: invalid format for image: svg).
// Exige PNG o JPEG cuadrado (preferiblemente 512x512).
//
// Uso: node svg-to-png.mjs <entrada.svg> <salida.png> [ancho] [alto]

import puppeteer from 'puppeteer';
import { readFileSync } from 'fs';

const [, , svgPath, outPng, w = '512', h = '512'] = process.argv;

if (!svgPath || !outPng) {
  console.error('Uso: node svg-to-png.mjs <entrada.svg> <salida.png> [ancho] [alto]');
  process.exit(1);
}

const width = Number(w);
const height = Number(h);
const svg = readFileSync(svgPath, 'utf8');

const browser = await puppeteer.launch({ args: ['--no-sandbox', '--disable-setuid-sandbox'] });
try {
  const page = await browser.newPage();
  await page.setViewport({ width, height, deviceScaleFactor: 1 });
  await page.setContent(
    `<!doctype html><html><head><style>html,body{margin:0;padding:0;overflow:hidden;background:transparent;}</style></head><body>${svg}</body></html>`,
    { waitUntil: 'networkidle0' }
  );

  await page.screenshot({
    path: outPng,
    omitBackground: true,
  });
  console.log(`Icono PNG generado: ${outPng} (${width}x${height})`);
} finally {
  await browser.close();
}
