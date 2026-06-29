# AGENTS.md

Guidance for work inside `keycloakify-theme`.

## Project Purpose

This folder is a standalone Keycloakify v11 React/Vite theme for the ecommerce product management app.

- Theme name: `ecommerce-products`
- Keycloak client: `ecommerce-api`
- Local Keycloak: `http://localhost:9090`
- Main customized pages:
  - `src/login/pages/Login.tsx`
  - `src/login/pages/Register.tsx`
  - `src/login/ecommerce-theme.css`

The Next.js app must continue redirecting to Keycloak for authentication. Do not reintroduce a custom Next.js login form.

## Implementation Rules

- Keep Keycloak security behavior intact. Preserve Keycloak form `action`, field names, hidden inputs, error rendering, password reveal behavior, recaptcha behavior, and WebAuthn/passkey hooks.
- Use Keycloakify page components and context types rather than hardcoding auth URLs.
- Use assets from `public/` for theme images. The current logo is `public/image/logo.png`.
- Keep captions and UI wording focused on ecommerce product management, catalog operations, inventory, categories, pricing, and protected Spring Boot API access.
- Avoid external image URLs in the theme. Keycloak CSP and offline local development may block them.
- Keep styling in `src/login/ecommerce-theme.css` unless a React component change is necessary.
- The dark polygon background is only for default Keycloak fallback pages. Login and register should use the light blue split-screen style.
- Do not duplicate the logo. Keep the main logo on the form side only unless explicitly requested otherwise.

## Build And Deploy

Use the package scripts from this folder:

```bash
npm run build
npm run build-keycloak-theme
npm run build:deploy
```

`build:deploy` copies the Keycloak 26 compatible JAR:

```text
dist_keycloak/keycloak-theme-for-kc-all-other-versions.jar
```

to:

```text
/Users/thornie/Documents/ITE-Gen3/Spring-Framework/ite-spring-boot/user/keycloak/providers/ecommerce-products-keycloak-theme.jar
```

After deployment, Keycloak must be restarted before the new JAR is loaded:

```bash
cd /Users/thornie/Documents/ITE-Gen3/Spring-Framework/ite-spring-boot/user
docker compose restart keycloak
```

## Important Build Note

Do not delete `dist_keycloak` in scripts. Keycloakify manages that folder internally, and deleting it before `keycloakify build` can cause packaging errors.

The safe clean step is only:

```bash
npm run clean:dist
```

## Verification

After visual or component changes, run:

```bash
npm run build:deploy
```

If only checking TypeScript/Vite output, run:

```bash
npm run build
```

Report whether the build passed and remind the user to restart Keycloak when the JAR was redeployed.
