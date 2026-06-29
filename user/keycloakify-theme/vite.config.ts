import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { keycloakify } from "keycloakify/vite-plugin";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react(),
        keycloakify({
            themeName: "ecommerce-products",
            artifactId: "ecommerce-products-keycloak-theme",
            groupId: "ecommerce.products.keycloak",
            accountThemeImplementation: "none"
        })
    ]
});
