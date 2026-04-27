# Open Collective Application Plan
**Project:** kinetic-core
**Target Host:** Open Source Collective (OSC)
**Goal:** Secure sustainable funding for open-source development.

---

## 1. Strategy Overview

**Model:** Open Core
- **Public Project (The Collective):** `kinetic-core` (The engine).
- **Commercial Backer (The User):** `kineticmcp` (The commercial product).

We will position `kinetic-core` as the **independent, community-driven standard** for Python-Salesforce integration, essential for the AI era. The existence of a commercial product built on top of it (`kineticmcp`) serves as social proof of its robustness, not a conflict of interest.

---

## 2. Application Prerequisites Checklist

Before submitting the form, we must ensure the repository looks "community-ready":

- [ ] **License Check:** Ensure `LICENSE` is clearly MIT. (Done ✅)
- [ ] **Code of Conduct:** Add a `CODE_OF_CONDUCT.md` (Standard Contributor Covenant).
- [ ] **Contribution Guidelines:** Ensure `CONTRIBUTING.md` exists and is welcoming.
- [ ] **Security Policy:** Add `SECURITY.md` (how to report vulnerabilities).
- [ ] **The "Why":** Update `README.md` to explicitly state the mission: *"Democratizing Salesforce integration for AI agents."*

---

## 3. The Pitch (Application Text Draft)

This is the text to use when applying to the Open Source Collective via their form.

**Project Name:** Kinetic Core
**Website:** https://kineticmcp.com/kinetic-core
**Repository:** https://github.com/KineticMCP/kinetic-core

**Description:**
Kinetic Core is a modern, production-ready Python library designed to be the standard engine for integrating AI agents with Salesforce. It solves the complexity of Salesforce's SOAP, REST, Bulk, and Metadata APIs, providing a clean, unified Pythonic interface.

**Why do you want to join Open Source Collective?**
We are building the foundational infrastructure that powers the next generation of AI tools for Salesforce. While we have a commercial product (KineticMCP) using this library, `kinetic-core` itself is and will always remain free and open source.
We want to join OSC to:
1. Allow enterprises that rely on our library to contribute financially to its stability.
2. Fund independent contributors, documentation writers, and security audits.
3. Establish a transparent, community-governed budget separate from any commercial entity.

**How is your community active?**
The project is currently driven by the core maintenance team but has started seeing traction in the MCP (Model Context Protocol) ecosystem. We release frequent updates (v2.1.0 just released with Metadata API support) and maintain high-quality documentation to lower the barrier for new Python developers entering the Salesforce ecosystem.

---

## 4. Funding Tiers Strategy

Define clear benefits for sponsors.

### Individuals
- **Backer ($5/mo):** "Support the maintenance of the library." -> Name in `BACKERS.md`.
- **Power User ($20/mo):** "You use Kinetic Core in your daily scripts." -> Priority bug reports.

### Organizations
- **Silver Sponsor ($100/mo):** "For startups building on Salesforce." -> Logo in README.md footer.
- **Gold Sponsor ($500/mo):** "For enterprises relying on Kinetic Core." -> Large Logo in README + Doc site sidebar.
- **Platinum Partner ($2,000/mo):** "Direct line to maintainers." -> 1 hour/mo advisory call.

---

## 5. Repository Assets to Create

We need to add these files to the repository to activate the funding flow.

### `.github/FUNDING.yml`
This adds the "Sponsor" button to the repo.

```yaml
# .github/FUNDING.yml
github: [Antoniotrento]
open_collective: kinetic-core
custom: ["https://kineticmcp.com"]
```

### `CODE_OF_CONDUCT.md`
Standard standard required by Open Collective. Use the [Contributor Covenant](https://www.contributor-covenant.org/).

### `SECURITY.md`
Simple policy stating how to report security issues (e.g., "Email security@kineticmcp.com").

---

## 6. Action Plan

1. **Prepare Repo:** Create the missing community files (`.github/FUNDING.yml`, `CODE_OF_CONDUCT.md`).
2. **Apply:** Go to [Open Source Collective Apply](https://opencollective.com/opensource/apply) and submit the pitch.
3. **Wait:** Approval usually takes 1-2 weeks.
4. **Launch:** Once approved, update README badges to show "Back this project".

---

**Next Step:** Shall we generate the `.github/FUNDING.yml` and `CODE_OF_CONDUCT.md` files now?
