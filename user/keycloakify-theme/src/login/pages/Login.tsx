import type { JSX } from "keycloakify/tools/JSX";
import { useState } from "react";
import { kcSanitize } from "keycloakify/lib/kcSanitize";
import { useIsPasswordRevealed } from "keycloakify/tools/useIsPasswordRevealed";
import type { PageProps } from "keycloakify/login/pages/PageProps";
import { getKcClsx, type KcClsx } from "keycloakify/login/lib/kcClsx";
import { clsx } from "keycloakify/tools/clsx";
import type { KcContext } from "../KcContext";
import type { I18n } from "../i18n";
import { useScript } from "keycloakify/login/pages/Login.useScript";

const logoSrc = `${import.meta.env.BASE_URL}image/logo.png`;

type LoginSocialProviders = NonNullable<NonNullable<Extract<KcContext, { pageId: "login.ftl" }>["social"]>["providers"]>;

export default function Login(props: PageProps<Extract<KcContext, { pageId: "login.ftl" }>, I18n>) {
    const { kcContext, i18n, doUseDefaultCss, Template, classes } = props;

    const { kcClsx } = getKcClsx({
        doUseDefaultCss,
        classes
    });

    const {
        social,
        realm,
        url,
        usernameHidden,
        login,
        auth,
        registrationDisabled,
        messagesPerField,
        enableWebAuthnConditionalUI,
        authenticators
    } = kcContext;

    const { msg, msgStr } = i18n;
    const [isLoginButtonDisabled, setIsLoginButtonDisabled] = useState(false);
    const webAuthnButtonId = "authenticateWebAuthnButton";
    const socialProviders = social?.providers;

    useScript({
        webAuthnButtonId,
        kcContext,
        i18n
    });

    return (
        <Template
            kcContext={kcContext}
            i18n={i18n}
            doUseDefaultCss={doUseDefaultCss}
            classes={classes}
            displayMessage={!messagesPerField.existsError("username", "password")}
            headerNode={null}
            displayInfo={false}
        >
            <div className="ecom-auth-shell">
                <aside className="ecom-auth-panel" aria-hidden="true">
                    <div className="ecom-panel-copy">
                        <h1>{msg("ecomLoginPanelTitle")}</h1>
                        <p>{msg("ecomLoginPanelBody")}</p>
                    </div>
                </aside>

                <div className="ecom-auth-card">
                    <div className="ecom-form-logo-container">
                        <div className="ecom-form-logo">
                            <img src={logoSrc} alt={msgStr("ecomLogoAlt")} />
                        </div>
                        <h2 className="ecom-form-logo-text">E-Commerce Products</h2>
                    </div>
                    <div className="ecom-card-header">
                        <h2>{msg("ecomLoginTitle")}</h2>
                        <p>{msg("ecomLoginSubtitle")}</p>
                    </div>

                    {realm.password && socialProviders !== undefined && socialProviders.length !== 0 && (
                        <SocialProviders kcClsx={kcClsx} i18n={i18n} providers={socialProviders} />
                    )}

                    <div id="kc-form">
                        <div id="kc-form-wrapper">
                            {realm.password ? (
                                <form
                                    id="kc-form-login"
                                    className="ecom-form"
                                    onSubmit={() => {
                                        setIsLoginButtonDisabled(true);
                                        return true;
                                    }}
                                    action={url.loginAction}
                                    method="post"
                                >
                                    {!usernameHidden && (
                                        <div className={kcClsx("kcFormGroupClass")}>
                                            <label htmlFor="username" className={kcClsx("kcLabelClass")}>
                                                {!realm.loginWithEmailAllowed
                                                    ? msg("username")
                                                    : !realm.registrationEmailAsUsername
                                                      ? msg("usernameOrEmail")
                                                      : msg("email")}
                                            </label>
                                            <input
                                                tabIndex={2}
                                                id="username"
                                                className={kcClsx("kcInputClass")}
                                                name="username"
                                                defaultValue={login.username ?? ""}
                                                type="text"
                                                autoFocus
                                                autoComplete={enableWebAuthnConditionalUI ? "username webauthn" : "username"}
                                                aria-invalid={messagesPerField.existsError("username", "password")}
                                                placeholder={msgStr("ecomLoginEmailPlaceholder")}
                                            />
                                            {messagesPerField.existsError("username", "password") && (
                                                <span
                                                    id="input-error"
                                                    className={kcClsx("kcInputErrorMessageClass")}
                                                    aria-live="polite"
                                                    dangerouslySetInnerHTML={{
                                                        __html: kcSanitize(messagesPerField.getFirstError("username", "password"))
                                                    }}
                                                />
                                            )}
                                        </div>
                                    )}

                                    <div className={kcClsx("kcFormGroupClass")}>
                                        <label htmlFor="password" className={kcClsx("kcLabelClass")}>
                                            {msg("password")}
                                        </label>
                                        <PasswordWrapper kcClsx={kcClsx} i18n={i18n} passwordInputId="password">
                                            <input
                                                tabIndex={3}
                                                id="password"
                                                className={kcClsx("kcInputClass")}
                                                name="password"
                                                type="password"
                                                autoComplete="current-password"
                                                aria-invalid={messagesPerField.existsError("username", "password")}
                                                placeholder={msgStr("ecomLoginPasswordPlaceholder")}
                                            />
                                        </PasswordWrapper>
                                        {usernameHidden && messagesPerField.existsError("username", "password") && (
                                            <span
                                                id="input-error"
                                                className={kcClsx("kcInputErrorMessageClass")}
                                                aria-live="polite"
                                                dangerouslySetInnerHTML={{
                                                    __html: kcSanitize(messagesPerField.getFirstError("username", "password"))
                                                }}
                                            />
                                        )}
                                    </div>

                                    <div className={kcClsx("kcFormGroupClass", "kcFormSettingClass")}>
                                        <div id="kc-form-options">
                                            {realm.rememberMe && !usernameHidden && (
                                                <label className="ecom-remember">
                                                    <input
                                                        tabIndex={5}
                                                        id="rememberMe"
                                                        name="rememberMe"
                                                        type="checkbox"
                                                        defaultChecked={!!login.rememberMe}
                                                    />
                                                    {msg("rememberMe")}
                                                </label>
                                            )}
                                        </div>
                                        <div className={kcClsx("kcFormOptionsWrapperClass")}>
                                            {realm.resetPasswordAllowed && (
                                                <a tabIndex={6} href={url.loginResetCredentialsUrl}>
                                                    {msg("doForgotPassword")}
                                                </a>
                                            )}
                                        </div>
                                    </div>

                                    <div id="kc-form-buttons" className={kcClsx("kcFormGroupClass")}>
                                        <input type="hidden" id="id-hidden-input" name="credentialId" value={auth.selectedCredential} />
                                        <input
                                            tabIndex={7}
                                            disabled={isLoginButtonDisabled}
                                            className={kcClsx("kcButtonClass", "kcButtonPrimaryClass", "kcButtonBlockClass", "kcButtonLargeClass")}
                                            name="login"
                                            id="kc-login"
                                            type="submit"
                                            value={msgStr("doLogIn")}
                                        />
                                    </div>
                                </form>
                            ) : (
                                <p className="ecom-error">{msg("ecomLoginPasswordDisabled")}</p>
                            )}
                        </div>
                    </div>

                    {enableWebAuthnConditionalUI && (
                        <PasskeyLogin
                            kcClsx={kcClsx}
                            url={url}
                            authenticators={authenticators}
                            webAuthnButtonId={webAuthnButtonId}
                            label={msgStr("passkey-doAuthenticate")}
                        />
                    )}

                    {realm.password && realm.registrationAllowed && !registrationDisabled && (
                        <p className="ecom-register-link">
                            {msg("ecomLoginRegisterPrompt")}{" "}
                            <a tabIndex={8} href={url.registrationUrl}>
                                {msg("doRegister")}
                            </a>
                        </p>
                    )}
                </div>
            </div>
        </Template>
    );
}

function SocialProviders(props: {
    kcClsx: KcClsx;
    i18n: I18n;
    providers: LoginSocialProviders;
}) {
    const { kcClsx, i18n, providers } = props;
    const { msg } = i18n;

    return (
        <div id="kc-social-providers" className={kcClsx("kcFormSocialAccountSectionClass")}>
            <h3>{msg("identity-provider-login-label")}</h3>
            <ul className={kcClsx("kcFormSocialAccountListClass", providers.length > 3 && "kcFormSocialAccountListGridClass")}>
                {providers.map(provider => (
                    <li key={provider.alias}>
                        <a
                            id={`social-${provider.alias}`}
                            className={kcClsx("kcFormSocialAccountListButtonClass", providers.length > 3 && "kcFormSocialAccountGridItem")}
                            type="button"
                            href={provider.loginUrl}
                        >
                            {provider.iconClasses && (
                                <i className={clsx(kcClsx("kcCommonLogoIdP"), provider.iconClasses)} aria-hidden="true" />
                            )}
                            <span
                                className={clsx(kcClsx("kcFormSocialAccountNameClass"), provider.iconClasses && "kc-social-icon-text")}
                                dangerouslySetInnerHTML={{ __html: kcSanitize(provider.displayName) }}
                            />
                        </a>
                    </li>
                ))}
            </ul>
            <div className="ecom-divider" aria-hidden="true">
                <span>{msg("ecomFormDivider")}</span>
            </div>
        </div>
    );
}

function PasswordWrapper(props: { kcClsx: KcClsx; i18n: I18n; passwordInputId: string; children: JSX.Element }) {
    const { kcClsx, i18n, passwordInputId, children } = props;
    const { msgStr } = i18n;
    const { isPasswordRevealed, toggleIsPasswordRevealed } = useIsPasswordRevealed({ passwordInputId });

    return (
        <div className={kcClsx("kcInputGroup")}>
            {children}
            <button
                type="button"
                className={kcClsx("kcFormPasswordVisibilityButtonClass")}
                aria-label={msgStr(isPasswordRevealed ? "hidePassword" : "showPassword")}
                aria-controls={passwordInputId}
                onClick={toggleIsPasswordRevealed}
            >
                <i className={kcClsx(isPasswordRevealed ? "kcFormPasswordVisibilityIconHide" : "kcFormPasswordVisibilityIconShow")} aria-hidden />
            </button>
        </div>
    );
}

function PasskeyLogin(props: {
    kcClsx: KcClsx;
    url: Extract<KcContext, { pageId: "login.ftl" }>["url"];
    authenticators: Extract<KcContext, { pageId: "login.ftl" }>["authenticators"];
    webAuthnButtonId: string;
    label: string;
}) {
    const { kcClsx, url, authenticators, webAuthnButtonId, label } = props;

    return (
        <div className="ecom-passkey">
            <form id="webauth" action={url.loginAction} method="post">
                <input type="hidden" id="clientDataJSON" name="clientDataJSON" />
                <input type="hidden" id="authenticatorData" name="authenticatorData" />
                <input type="hidden" id="signature" name="signature" />
                <input type="hidden" id="credentialId" name="credentialId" />
                <input type="hidden" id="userHandle" name="userHandle" />
                <input type="hidden" 
                id="error" name="error" />
            </form>

            {authenticators !== undefined && authenticators.authenticators.length !== 0 && (
                <form id="authn_select" className={kcClsx("kcFormClass")}>
                    {authenticators.authenticators.map((authenticator, i) => (
                        <input key={i} type="hidden" name="authn_use_chk" readOnly value={authenticator.credentialId} />
                    ))}
                </form>
            )}

            <input
                id={webAuthnButtonId}
                type="button"
                className={kcClsx("kcButtonClass", "kcButtonDefaultClass", "kcButtonBlockClass", "kcButtonLargeClass")}
                value={label}
            />
        </div>
    );
}
