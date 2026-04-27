# Política de Seguridad - DrakesCraft Labs 🛡️

Esta política de seguridad describe cómo el equipo de **DrakesCraft Labs** gestiona las vulnerabilidades y cómo puedes reportarlas de forma segura.

## Versiones Soportadas

Actualmente, solo proporcionamos parches de seguridad para la rama de modernización principal:

| Versión | Soportada |
| ------- | --------- |
| 1.21.1+ (Modernización) | ✅ Sí |
| < 1.20.x (Legacy) | ❌ No |

## Reportar una Vulnerabilidad

**¡Por favor, NO abras un Issue público para reportar una vulnerabilidad de seguridad!**

Si descubres un problema de seguridad en nuestro ecosistema de Slimefun, te agradecemos que lo reportes de forma responsable:

1. **Contacto privado**: envía un reporte detallado por los canales privados de **DrakesCraft Labs** (Discord de la organización o correo de contacto del laboratorio si está habilitado).
2. **Detalles**: Incluye una descripción del fallo, pasos para reproducirlo y el impacto potencial.
3. **Tiempo de Respuesta**: Nos comprometemos a acusar recibo en menos de 48 horas y a trabajar en una solución inmediata.

## Nuestra Filosofía de Seguridad

En **DrakesCraft Labs**, aplicamos una política de **"Zero Trust"** para librerías de terceros:
- **Shadow Patching**: Sombreamos y parcheamos librerías críticas (como `commons-lang`) para eliminar CVEs heredados.
- **Auditoría Continua**: Utilizamos GitHub Dependabot para monitorizar dependencias en tiempo real.
- **Aislamiento de Namespaces**: El uso de namespaces propios (`dev.drake`) evita colisiones y ataques de "dependency confusion".

Agradecemos a la comunidad por ayudarnos a mantener el reactor Slimefun seguro y estable. 🚀☢️

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
