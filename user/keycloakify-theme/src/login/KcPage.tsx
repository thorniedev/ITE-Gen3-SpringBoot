import { Suspense, lazy } from "react";
import type { ClassKey } from "keycloakify/login";
import type { KcContext } from "./KcContext";
import { useI18n } from "./i18n";
import DefaultPage from "keycloakify/login/DefaultPage";
import Template from "keycloakify/login/Template";
import "./ecommerce-theme.css";

const UserProfileFormFields = lazy(
    () => import("keycloakify/login/UserProfileFormFields")
);
const Login = lazy(() => import("./pages/Login"));
const Register = lazy(() => import("./pages/Register"));

const doMakeUserConfirmPassword = true;

export default function KcPage(props: { kcContext: KcContext }) {
    const { kcContext } = props;

    const { i18n } = useI18n({ kcContext });

    return (
        <Suspense>
            {(() => {
                switch (kcContext.pageId) {
                    case "login.ftl":
                        return (
                            <Login
                                {...{ kcContext, i18n, classes }}
                                Template={Template}
                                doUseDefaultCss={false}
                            />
                        );
                    case "register.ftl":
                        return (
                            <Register
                                {...{ kcContext, i18n, classes }}
                                Template={Template}
                                doUseDefaultCss={false}
                                UserProfileFormFields={UserProfileFormFields}
                                doMakeUserConfirmPassword={doMakeUserConfirmPassword}
                            />
                        );
                    default:
                        return (
                            <DefaultPage
                                kcContext={kcContext}
                                i18n={i18n}
                                classes={classes}
                                Template={Template}
                                doUseDefaultCss={false}
                                UserProfileFormFields={UserProfileFormFields}
                                doMakeUserConfirmPassword={doMakeUserConfirmPassword}
                            />
                        );
                }
            })()}
        </Suspense>
    );
}

const classes = {
    kcLoginClass: "ecom-kc-page",
    kcFormCardClass: "ecom-default-card",
    kcFormHeaderClass: "ecom-default-header",
    kcContentWrapperClass: "ecom-content-wrapper",
    kcInfoAreaWrapperClass: "ecom-info-wrapper",
    kcSignUpClass: "ecom-info-area",
    kcFormClass: "ecom-form",
    kcFormGroupClass: "ecom-field",
    kcFormSettingClass: "ecom-form-row",
    kcFormOptionsClass: "ecom-form-options",
    kcFormOptionsWrapperClass: "ecom-form-link",
    kcFormButtonsClass: "ecom-actions",
    kcInputClass: "ecom-input",
    kcInputGroup: "ecom-password",
    kcInputWrapperClass: "ecom-input-wrapper",
    kcLabelClass: "ecom-label",
    kcLabelWrapperClass: "ecom-checkbox-row",
    kcInputErrorMessageClass: "ecom-error",
    kcCheckboxInputClass: "ecom-checkbox",
    kcButtonClass: "ecom-button",
    kcButtonPrimaryClass: "ecom-button-primary",
    kcButtonDefaultClass: "ecom-button-secondary",
    kcButtonBlockClass: "ecom-button-block",
    kcButtonLargeClass: "ecom-button-large",
    kcFormPasswordVisibilityButtonClass: "ecom-password-toggle",
    kcFormPasswordVisibilityIconShow: "ecom-password-icon ecom-password-icon-show",
    kcFormPasswordVisibilityIconHide: "ecom-password-icon ecom-password-icon-hide",
    kcFormSocialAccountSectionClass: "ecom-social-section",
    kcFormSocialAccountListClass: "ecom-social-list",
    kcFormSocialAccountListGridClass: "ecom-social-grid",
    kcFormSocialAccountListButtonClass: "ecom-social-button",
    kcFormSocialAccountGridItem: "ecom-social-grid-item",
    kcFormSocialAccountNameClass: "ecom-social-name",
    kcCommonLogoIdP: "ecom-social-logo"
} satisfies { [key in ClassKey]?: string };
