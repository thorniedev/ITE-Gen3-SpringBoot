# Keycloak Ecommerce Auth Form Design

Use this brief when redesigning the Keycloakify login/register pages for this project.

## Scope

This design is for Keycloak only. Implement it inside the Keycloakify theme, not inside the Next.js app.

Primary files:

- `src/login/pages/Login.tsx`
- `src/login/pages/Register.tsx`
- `src/login/ecommerce-theme.css`

Keep Keycloak security behavior unchanged. Preserve form actions, field names, hidden inputs, validation messages, password reveal, recaptcha, and WebAuthn/passkey logic.

## Local Assets

Use only local files from `public/`.

- Logo: `public/image/logo.png`
- Design reference image: `public/image/sample-form.png`

Do not use external image URLs.

In React, load the logo with:

```ts
const logoSrc = `${import.meta.env.BASE_URL}image/logo.png`;
```

Use the sample image only as a visual reference. Do not render the sample image as the final UI.

## Visual Direction

Create a polished ecommerce login/register form inspired by `public/image/sample-form.png`:

- Full-screen purple/blue gradient background.
- Large decorative abstract shape on the left/top area using CSS gradients or pseudo-elements.
- Two-column layout on desktop:
  - Left side: brand, ecommerce-focused copy, and supporting actions/text.
  - Right side: glass-style login/register card.
- Use one logo only, preferably inside the form card header.
- No duplicate logos.
- No generic text like "Lucy", "Adventure", "Innovation Starts Here", "Future Applications", or "What to Expect".
- Keep the UI clearly related to ecommerce product management.

## Approved Copy

Login page:

- Heading: `Sign in to Product Manager`
- Subheading: `Access your ecommerce catalog, inventory, pricing, and product media.`
- Left panel title: `Control your ecommerce catalog`
- Left panel body: `Manage products, categories, stock, and prices from a protected Keycloak session.`
- Remember label: keep Keycloak default message.
- Forgot password: keep Keycloak default message.
- Register prompt: `New to the dashboard?` followed by the Keycloak register link.

Register page:

- Heading: `Create your product workspace`
- Subheading: `Register for secure access to ecommerce product operations.`
- Left panel title: `Launch product operations securely`
- Left panel body: `Create an account for catalog management, inventory updates, pricing, and media workflows.`
- Back to login: keep Keycloak default message.

Default Keycloak pages:

- Keep copy from Keycloak.
- Style them consistently with the same purple/blue background and centered card.

## Layout Requirements

Desktop:

- Page fills the viewport.
- Center a wide auth shell with max width around `1100px`.
- Left panel uses bold white text and decorative shapes.
- Right card uses translucent purple glass style:
  - background: semi-transparent purple
  - border: subtle white border
  - backdrop blur
  - rounded corners
  - soft shadow
- Form controls should be large, readable, and aligned.

Tablet:

- Keep two columns if width allows.
- Reduce text size and panel padding.

Mobile:

- Switch to a single-column layout.
- Hide or compact the left decorative panel.
- Keep the form card centered with full-width inputs.
- Avoid horizontal scrolling.

## Form Styling

Inputs:

- Rounded corners.
- Light border with subtle transparency.
- Purple glass background or white translucent background.
- Clear focus state with cyan/blue glow.
- Error state must stay visible in red.

Buttons:

- Primary button uses cyan/light-blue accent.
- Hover: slight lift and brighter shadow.
- Disabled: reduced opacity, no lift.

Links:

- Use light/cyan accent on dark cards.
- Keep underlines only on hover.

## Animations

Use subtle CSS animations only:

- Auth shell fades in and moves up slightly on load.
- Decorative shapes slowly float.
- Form card has a soft entrance animation.
- Buttons lift on hover.
- Inputs glow on focus.

Respect reduced motion:

```css
@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        scroll-behavior: auto !important;
        transition-duration: 0.01ms !important;
    }
}
```

## Accessibility

- Keep all labels connected to inputs.
- Keep Keycloak error messages with `aria-live` where already present.
- Maintain keyboard navigation.
- Do not remove autofill/autocomplete attributes.
- Keep contrast readable on purple backgrounds.

## Build And Deploy

After changes, run from `keycloakify-theme`:

```bash
npm run build:deploy
```

Then restart Keycloak:

```bash
cd /Users/thornie/Documents/ITE-Gen3/Spring-Framework/ite-spring-boot/user
docker compose restart keycloak
```

Do not delete `dist_keycloak`. Keycloakify manages it internally.
