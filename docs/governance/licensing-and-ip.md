# Licensing and intellectual-property policy

**Status:** Project policy for review; not legal advice or a rights certification.

## 1. Current repository notice

The root `LICENSE` currently contains a project-specific proprietary evaluation notice. It is not an OSI-approved open-source licence and does not grant a general right to redistribute or commercially exploit original proprietary material.

This update intentionally does not replace that notice. A public GitHub repository is not automatically open source. GitHub's platform terms and the rights necessary to view or fork a public repository are distinct from a general licence to use, modify, redistribute, or commercialise the project.

Before inviting external contributions or distributing binaries, the owner should decide whether the project remains proprietary, adopts a dual-licensing model, or releases specific components under an open-source licence.

## 2. Rights are component-specific

The repository must distinguish:

1. Original project code and documentation.
2. Third-party libraries and their licences.
3. Provider data, metadata, imagery, and API outputs.
4. Maps, tiles, geocoding, routing, and basemap assets.
5. Logos, trademarks, and institutional identity assets.
6. Fonts, icons, illustrations, photographs, and AI-assisted artwork.
7. User-submitted content and personal data.

A project-level notice cannot override a third-party licence, provider terms, database rights, privacy law, or mandatory legal exceptions.

## 3. Required third-party inventory

For each dependency or asset, record:

- Name and version or dataset identifier.
- Copyright/rights holder.
- Source URL and retrieval date.
- Licence or terms URL and applicable version.
- Attribution text and placement requirements.
- Redistribution, caching, derivative-work, and commercial-use conditions.
- Geographic or temporal restrictions.
- Review owner and review date.
- Status: approved, conditional, blocked, or unknown.

Do not use a provider's logo merely because its data is accessible. Use plain-text attribution and an official link until brand permissions and identity rules have been reviewed.

## 4. Provider-specific caution

NASA, USGS, GDACS, MET Norway, NOAA, ESA/Copernicus, Mapbox, and other providers may have different terms for data, metadata, imagery, APIs, tiles, logos, and derived products. Rights must be checked at the asset and endpoint level. Never infer a blanket licence from the provider name.

The current [rights register](../rights-register.json) records existing restrictions and unresolved items. Keep it current as integrations change.

## 5. Contributions

A pull request does not prove that the contributor owns every submitted element. Contributors must identify third-party material and confirm that they have authority to submit it under the project's applicable terms.

The project has not established a signed Contributor Licence Agreement or copyright assignment process in this repository. Do not imply that one exists. Before accepting substantial external code contributions, the owner should decide whether a CLA, DCO, or another contributor-rights process is required.

## 6. Trademarks and business identity

BASTION-KERN and OFFENTLOGSPOT are project identifiers. The use of ™ is a claim of trademark use, not evidence of registration. Do not use ® or claim registration unless verified in the relevant jurisdiction.

Do not claim that OFFENTLOGSPOT is an incorporated company, regulated entity, government body, or authorised emergency service unless the corresponding legal status and permissions are documented.

## 7. Required decisions before commercial release

- Confirm chain of title for original code, designs, and commissioned work.
- Complete trademark clearance and decide filing jurisdictions.
- Review all dependency and provider licences for commercial use.
- Verify map, geocoding, routing, and imagery terms.
- Define the contributor-rights model.
- Identify the contracting legal entity and service address.
- Review privacy, consumer, tax, and product terms in each launch market.

## 8. Change control

Any change to the root `LICENSE` requires explicit owner approval and review of all affected files. Never silently replace the proprietary notice with MIT, Apache-2.0, GPL, or another licence.
