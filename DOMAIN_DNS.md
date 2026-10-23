# Activación de bastion-kern.app

Estado: 23 de octubre de 2026. La página actualizada está publicada en Sites. `bastion-kern.app` está vinculado a ella y pendiente de validación DNS y certificado TLS. Se retiró la solicitud anterior para `bastion-kern.com`; no se compró ni transfirió ningún dominio.

## Registros para la página actual

Estos valores corresponden al alojamiento actual en Sites. No son los registros para publicar la aplicación de Lovable.

| Tipo | Host relativo | Valor | TTL |
|---|---|---|---|
| A | `@` | `162.159.143.30` | 300 |
| A | `@` | `172.66.3.26` | 300 |
| TXT | `_openai-site-verification` | `openai-site-verification=Fj_tu4m_vJpe7uDJbWhBV3kPLbgy6jmF8ns41_4iNZg` | 300 |
| TXT | `_cf-custom-hostname` | `3e588b3f-1f31-433d-8a13-c27cfd6e9fee` | 300 |

Si el panel pide nombres completos para los TXT, añade `.bastion-kern.app` a cada host TXT. Usa ambos A en `@`. Conserva MX y los TXT ajenos a esta verificación; revisa los registros web A/AAAA/CNAME que puedan entrar en conflicto antes de modificarlos.

## Si administras el dominio en Lovable

Abre Workspace settings → Workspace domains → Configure junto a bastion-kern.app → DNS records. Se requiere rol de administrador o propietario. Si cambiaste los nameservers a otro proveedor, los registros se editan allí.

Si aparecen A bloqueados creados automáticamente por Lovable, el dominio está conectado a un proyecto: antes de sustituir su destino web debe revisarse ese proyecto. Cambiar el destino enviará los visitantes a la página actualizada de Sites. Los registros A de dos alojamientos distintos no deben mezclarse como forma de conectar ambos sitios.

La documentación de Lovable indica que los registros automáticos se desbloquean al desconectar el dominio del proyecto; no se ha ejecutado esa desconexión ni se ha cambiado ningún DNS desde aquí. El conector disponible no ofrece controles DNS.

Guía oficial: https://docs.lovable.dev/features/custom-domain#add-or-edit-dns-records-for-a-domain-bought-through-lovable

Una vez aplicados los registros, falta verificar dominio y certificado antes de usar https://bastion-kern.app como URL activa. La aplicación de Lovable y la presentación de Sites son proyectos independientes; no se ha migrado ni sobrescrito la aplicación.
