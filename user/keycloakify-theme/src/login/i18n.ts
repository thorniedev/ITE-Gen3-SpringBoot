/* eslint-disable @typescript-eslint/no-unused-vars */
import { i18nBuilder } from "keycloakify/login";
import type { ThemeName } from "../kc.gen";

/** @see: https://docs.keycloakify.dev/features/i18n */
const { useI18n, ofTypeI18n } = i18nBuilder
    .withThemeName<ThemeName>()
    .withCustomTranslations({
        en: {
            ecomLogoAlt: "Ecommerce product manager logo",
            ecomLoginPanelKicker: "Ecommerce product operations",
            ecomLoginPanelTitle: "Control your ecommerce catalog",
            ecomLoginPanelBody: "Manage products, categories, stock, and prices from a protected Keycloak session.",
            ecomLoginMetricCatalog: "Catalog",
            ecomLoginMetricInventory: "Inventory",
            ecomLoginMetricPricing: "Pricing",
            ecomLoginEyebrow: "Secure catalog access",
            ecomLoginTitle: "Sign in to Product Manager",
            ecomLoginSubtitle: "Access your ecommerce catalog, inventory, pricing, and product media.",
            ecomLoginEmailPlaceholder: "name@example.com",
            ecomLoginPasswordPlaceholder: "Enter password",
            ecomLoginPasswordDisabled: "Password login is disabled for this realm.",
            ecomLoginRegisterPrompt: "New to the dashboard?",
            ecomFormDivider: "or",
            ecomRegisterPanelKicker: "Protected product workspace",
            ecomRegisterPanelTitle: "Launch product operations securely",
            ecomRegisterPanelBody: "Create an account for catalog management, inventory updates, pricing, and media workflows.",
            ecomRegisterMetricProducts: "Products",
            ecomRegisterMetricStock: "Stock",
            ecomRegisterMetricMedia: "Media",
            ecomRegisterEyebrow: "Secure workspace setup",
            ecomRegisterTitle: "Create your product workspace",
            ecomRegisterSubtitle: "Register for secure access to ecommerce product operations."
        }
    })
    .build();

type I18n = typeof ofTypeI18n;

export { useI18n, type I18n };
