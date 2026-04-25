# Smoke Runtime 1.21

Base de smoke test para validar que los jars no solo compilan: tambien arrancan en un servidor Paper real.

## Perfiles

- `foundation`: Paper 1.21.1 + Slimefun core Drake. Es el perfil por defecto y el que debe estar verde siempre.
- `core-addons`: Slimefun core + un grupo pequeno de addons Maven de bajo riesgo. Usalo para ampliar cobertura runtime cuando el stack base ya esta estable.

Los perfiles viven en `scripts/smoke/smoke-profiles.json`.

## Uso local

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\build-smoke-artifacts.ps1 -Profile foundation -Clean
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\run-smoke-server.ps1 -Profile foundation -Clean -TimeoutSeconds 120
```

El runner:

1. empaqueta los modulos del perfil;
2. copia jars a `.smoke/<perfil>/artifacts/plugins`;
3. descarga Paper `1.21.1` si falta;
4. acepta la EULA en el workspace temporal;
5. arranca el servidor con `--nogui`;
6. espera `Done`;
7. apaga con `stop`;
8. falla si detecta errores de carga o si no aparece el banner Drake.

## GitHub Actions

El workflow manual `Smoke Runtime 1.21` ejecuta el perfil `foundation` en Ubuntu. No corre en cada push para no ensuciar Actions; se dispara con `workflow_dispatch` cuando quieras validar runtime real.

## Banner esperado

Durante el arranque debe aparecer un banner verde de DrakesCraft con:

- `JACKSTAR`
- `DRAKESCRAFT`
- `CHAGUI68`
- enlace del repo
- enlace del perfil de JackStar

Ese marcador tambien sirve para confirmar que se cargo el jar Drake y no un artifact viejo.
