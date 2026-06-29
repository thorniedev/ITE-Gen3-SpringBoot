import { createRoot } from "react-dom/client";
import { StrictMode } from "react";
import { KcPage } from "./kc.gen";
import { getKcContextMock } from "./login/KcPageStory";

if (import.meta.env.DEV) {
    window.kcContext ??= getKcContextMock({
        pageId: "login.ftl",
        overrides: {
            realm: {
                displayName: "Ecommerce Products",
                registrationAllowed: true,
                resetPasswordAllowed: true,
                rememberMe: true
            },
            url: {
                registrationUrl: "#register-preview",
                loginResetCredentialsUrl: "#reset-preview"
            },
            social: {
                providers: []
            }
        }
    });
}

createRoot(document.getElementById("root")!).render(
    <StrictMode>
        {!window.kcContext ? (
            <h1>No Keycloak Context</h1>
        ) : (
            <KcPage kcContext={window.kcContext} />
        )}
    </StrictMode>
);
