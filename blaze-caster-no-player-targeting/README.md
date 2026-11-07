# Blaze Caster No Player Targeting

A server-side NeoForge add-on intended for this exact environment:

- Minecraft 1.21.1
- NeoForge 21.1.262
- Create: Wizardry `1.21.1-0.5.1-pre1`
- Iron's Spells 'n Spellbooks `1.21.1-3.16.3`

## Intended behavior

1. The Create: Wizardry Blaze Caster rejects players as targets, while still being able to target non-player living entities.
2. Mobs tracked by Create: Wizardry as summons from a Blaze Caster cast are marked with an entity tag.
3. Only those marked summons are prevented from acquiring players through `Mob#setTarget`.

Ordinary mobs and summons cast directly by players are not intentionally changed. No OPAC or FTB Teams integration is included.

**Scope:** this changes target acquisition only. It does not guarantee immunity to area damage, projectiles, explosions, or spell effects that hit players without selecting them as an AI target.

## Build it on GitHub (no local Gradle installation)

1. Download this project ZIP from the ChatGPT response.
2. Open [GitHub](https://github.com/) and sign in.
3. Create a new, empty repository. A name such as `blaze-caster-no-player-targeting` is fine. Do not initialize it with a README, license, or `.gitignore`.
4. Extract this ZIP on your computer.
5. Open the new repository on GitHub, choose **Add file → Upload files**, and upload the *contents* of the extracted `blaze-caster-no-player-targeting` folder. The `.github/workflows/build.yml` file must be included, with `.github` at the repository root.
6. Commit the upload to `main` (or `master`). GitHub Actions should start automatically. You can also start it manually from the **Actions → Build mod → Run workflow** page.
7. Open the completed workflow run. Under **Artifacts**, download `blaze-caster-no-player-targeting`.
8. Extract the downloaded artifact ZIP. It contains the mod JAR. Put that JAR in the server's `mods` folder.

The artifact is retained by GitHub for 14 days. A failed workflow does not produce a usable JAR; open the failed run and copy the first compiler/build error for diagnosis.

## Compatibility and validation status

The mixin targets were prepared against the supplied Create: Wizardry JAR and the version information above. The project has **not yet been compiled or tested in-game**. A successful GitHub build verifies compilation and packaging, but a server test is still needed to verify runtime mixin application and behavior.

The Create: Wizardry target uses its own class/method names (`remap = false`). The Minecraft `Mob` mixin uses normal mapping so the `setTarget` method can be remapped for the production runtime.
