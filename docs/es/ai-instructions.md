# Instrucciones para la IA

## Contexto Base

Este repo es un laboratorio de migración para el ecosistema Slimefun 4 (fork Drake), `Paper 1.21.1`, `Java 21` y las coordenadas compartidas del `pom.xml` raiz.

No es un único plugin.

## Reglas de Trabajo

- leer primero `README.md` y `docs/es/PLUGIN_MATRIX.md` (regenerar con `python scripts/generate_plugin_matrix.py` si cambiaron los gates)
- usar builds aislados con `-pl` y `-am`
- no compilar el reactor completo salvo necesidad estricta
- revisar primero si el fallo es de Maven o de API
- sincronizar documentación si cambia el estado del tablero

## Comando de referencia

```powershell
mvn -pl ruta/del/modulo -am -DskipTests package
```

## Criterio de investigación

Si el addon ya usa `dev.drake.dough.*` pero falla por imports, revisar primero si:

- hereda del parent
- declara `dough-core`
- sigue apuntando a coordenadas viejas de `Slimefun`

## Criterio de cierre

No dejar cambios ambiguos o a medio mezclar. Si no queda listo, dejar documentado con precisión por qué sigue fallando.

## Enlaces Relacionados

- [[AI-Start-Prompt]]
- [[Checklist de Migración]]
- [[Roadmap de Estabilización]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
