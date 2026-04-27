# GitHub Sponsors Activation Guide

**Project:** kinetic-core
**Goal:** Enable the "Sponsor" button and start receiving funding via GitHub.

---

## 1. Prerequisites (Done ✅)

The repository is already configured. We have created `.github/FUNDING.yml` pointing to:
```yaml
github: [antoniotrento]
```
This means funds will be directed to the personal account `@antoniotrento`.

---

## 2. Activation Steps

### A. Apply for GitHub Sponsors

1.  **Navigate to Dashboard:**
    - Go to [github.com/sponsors/dashboard](https://github.com/sponsors/dashboard).
    - **Status Check:** Use this link to check approval status.
    - **Organization Dashboard:** [github.com/sponsors/KineticMCP/dashboard](https://github.com/sponsors/KineticMCP/dashboard) (for KineticMCP)
    - Or visit your profile -> "Sponsors" (if visible).

2.  **Join the Waitlist (if not active):**
    - Ensure you are applying as **Antonio Trento** (Individual).
    - *Note:* If you prefer to receive funds as the organization, you must switch context to `KineticMCP`, but this often requires verifiable business documents immediately. Individual accounts are faster to approve.

3.  **Complete Profile:**
    - **Short Bio:** "Creator of kinetic-core, building the open standard for Salesforce AI integration."
    - **Introduction:** Explain why funding matters (server costs, time investment, ecosystem growth).

### B. Verify Identity & Bank Info (Stripe)

Once approved (usually 24-48h for active contributors):
1.  **Stripe Connect:** GitHub uses Stripe. You will be redirected to Stripe to add:
    - Legal Name
    - Date of Birth
    - IBAN / Bank Account details

### C. Create Funding Tiers

You must define "Tiers" for people to select. Recommended structure:

| Tier Name | Amount | Benefits |
|-----------|--------|----------|
| **Supporter** | $5 / mo | "Buy me a coffee" - Public badge on your profile. |
| **Contributor** | $25 / mo | Priority issue triage. |
| **Sponsor** | $100 / mo | Logo on project README.md (Small). |
| **Enterprise** | $500 / mo | Logo on main documentation site + 1hr support call. |

---

## 3. Post-Activation Checklist

Once your profile is live:

1.  **Check the Repo:**
    - Go to `https://github.com/KineticMCP/kinetic-core`.
    - Verify the **"Sponsor"** button appears at the top (next to Watch/Fork/Star).
    - Click it to verify it opens your payment tiers.

2.  **Promote:**
    - Add a "Sponsor me" link to your LinkedIn/Twitter bio.
    - Mention it in Release Notes (e.g., "Thanks to our sponsors...").

---

## 4. FAQ

**Q: Does GitHub take a fee?**
A: No! GitHub Sponsors charges **zero fees** (0%) for individual accounts. You get 100% of the money (minus Stripe's standard processing fees, if any). This is better than Open Collective (~10% fees).

**Q: Can I use both GitHub Sponsors and Open Collective?**
A: Yes! Our `FUNDING.yml` supports multiple platforms. Users will see a dropdown to choose their preferred method.
