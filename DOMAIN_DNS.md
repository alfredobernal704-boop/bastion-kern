# Activación de bastion-kern.com

Estado comprobado: 23 de octubre de 2026. Dominio vinculado al sitio, pendiente de validación DNS y certificado TLS. No se ha acreditado acceso al panel DNS desde las conexiones disponibles. No se compró ni transfirió ningún dominio.

Publicar estos registros únicamente en la zona DNS de bastion-kern.com que controles:

| Tipo | Nombre/host relativo | Valor |
|---|---|---|
| A | `@` | `162.159.143.30` |
| A | `@` | `172.66.3.26` |
| TXT | `_openai-site-verification` | `openai-site-verification=xZsV5ioxJPmiptW2oBH8DSWPa6qJITinCm_AJDVkOZY` |
| TXT | `_cf-custom-hostname` | `ab3c2b2a-8acf-4091-927e-10bdf765fea1` |

Algunos paneles piden el nombre completo para los TXT; en ese caso añade `.bastion-kern.com` al nombre indicado. Para el dominio raíz se usan los dos registros A; no añadas un CNAME de subdominio en `@`.

Conserva los registros MX y los TXT ajenos a esta verificación. Revisa los A/AAAA/CNAME previos que entren en conflicto con el destino web antes de cambiarlos. Este archivo no modifica el correo ni acredita la propiedad del dominio.

Tras publicar los registros, debe completarse la validación del dominio y del certificado. Solo después se podrá usar https://bastion-kern.com como URL pública. Hasta entonces sigue vigente el alojamiento temporal. La validación DNS no equivale a un registro de marca.
