# Ecommerce Products Keycloakify Theme

Custom Keycloak login/register theme for the Next.js ecommerce product manager.

This theme keeps security in Keycloak. The Next.js app still redirects to Keycloak, and Keycloak renders these React-built `login.ftl` and `register.ftl` pages.

## Step 1: Initialize

This folder is a dedicated Keycloakify v11 React project generated from the official starter:

```bash
cd /Users/thornie/Documents/ITE-Gen3/Front-end/NextJS/lab2-nextjs/keycloakify-theme
npm install
```

## Step 2: Extract Login / Register

The pages were ejected with:

```bash
npx keycloakify eject-page
```

Extracted pages:

- `src/login/pages/Login.tsx`
- `src/login/pages/Register.tsx`

They are routed from `src/login/KcPage.tsx`.

## Step 3: Customize In React

The ecommerce UI is implemented in:

- `src/login/pages/Login.tsx`
- `src/login/pages/Register.tsx`
- `src/login/ecommerce-theme.css`

Theme name configured in `vite.config.ts`:

```ts
themeName: "ecommerce-products"
```

## Step 4: Build The JAR

Keycloakify needs Java and Maven available on your machine.

```bash
npm run build-keycloak-theme
```

Output:

```text
dist_keycloak/keycloak-theme-for-kc-all-other-versions.jar
dist_keycloak/keycloak-theme-for-kc-22-to-25.jar
```

Your Keycloak container uses `quay.io/keycloak/keycloak:26.0.7`, so use:

```text
dist_keycloak/keycloak-theme-for-kc-all-other-versions.jar
```

## Step 5: Deploy To Local Keycloak

Copy the JAR into the Spring Boot Keycloak provider mount:

```bash
cp dist_keycloak/keycloak-theme-for-kc-all-other-versions.jar /Users/thornie/Documents/ITE-Gen3/Spring-Framework/ite-spring-boot/user/keycloak/providers/ecommerce-products-keycloak-theme.jar
```

Restart Keycloak:

```bash
cd /Users/thornie/Documents/ITE-Gen3/Spring-Framework/ite-spring-boot/user
docker compose restart keycloak
```

Enable it in Keycloak:

1. Open `http://localhost:9090`.
2. Log in to the `ite` realm admin console.
3. Go to `Realm settings` -> `Themes`.
4. Set `Login theme` to `ecommerce-products`.
5. Save.

You can also enable it per client:

1. Go to `Clients` -> `ecommerce-api`.
2. Find `Login theme`.
3. Select `ecommerce-products`.
4. Save.

Your Next.js login remains:

```text
http://localhost:3000/api/auth/login?returnTo=/product-table
```

It should redirect to Keycloak and show this custom theme.
