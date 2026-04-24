# Genetic Chickengineering Reborn

Rewritten by ybw0014.

![License](https://img.shields.io/github/license/ybw0014/GeneticChickengineering-Reborn) 

## Download

Download from:

- [Blob builds](https://blob.build/project/GeneticChickengineering-Reborn)
- [Guizhan Builds![Build Status](https://builds.guizhanss.com/ybw0014/GeneticChickengineering-Reborn/master/badge.svg)](https://builds.guizhanss.com/ybw0014/GeneticChickengineering-Reborn/master)

## Overview

Genetic Chickengineering is an original implementation of resource chickens
heavily inspired by SetyCz's popular Forge mod Chicken. 
The main difference between the two is that while Chicken has a predetermined
tree of chicken breeding, Genetic Chickengineering uses a very basic simulation
of genetics to determine chicken progression (meaning you may want to brush up
on [Punnett squares](https://en.wikipedia.org/wiki/Punnett_square)).

This is a mid- to late-game addon for
[Slimefun](https://github.com/Slimefun/Slimefun4). Furthermore, it uses
Paper-specific API calls, and as such will not work on Spigot servers.

## Plugin Basics
*See the*
[*wiki*](https://docs.ybw0014.dev/genetic-chickengineering/)
*for more detailed information*

Overworld chickens have almost completely become the dominant, "normal" chickens
that we know and love today, however there are certain chickens that carry
latent powers. With the right tools, time, and care, these latent powers may be
able to be uncovered, and the true potential of chickens can be unlocked.

The first step in genetically enhancing your chickens is to craft a Chicken Net
and turn some chickens into Pocket Chickens. Next, build a Genetic Sequencer to
analyze the Pocket Chicken and learn its genotypes. When you've collected two or
more favorable chickens, craft a Private Coop and leave your chickens to their
business. After some time, they'll roll the genetic dice and make a baby! From
there, release the chicken and let them grow into an adult, and the cycle can
continue.

Eventually, your efforts will be rewarded with a special chicken that can
produce resources! However, they can't do it alone... This is where the
Excitation Chamber comes into play. Insert a chicken with at least one
homozygous recessive pair of alleles (one set of two lower-case letters) and
it will begin producing a resource! Not all chickens of a resource type will
produce resources at the same rate, though. Due to interference from
heterozygous allele pairs (one dominant and one recessive), chickens which have
only homozygous pairs will make resources faster than those with heterozygous
genes. Add some cargo nodes to the Excitation Chamber to keep a steady,
neverending supply of different resources!

See
[the sequence table](https://docs.ybw0014.dev/genetic-chickengineering/misc/sequencing-guide)
or your in-game Slimefun Guide to learn how to make specific resources, or just
start breeding chickens like crazy until you have all 64 varieties!

## Images

![A basic machine overview](/images/gce_machines.png)

------

![A baby chicken fresh out of the Genetic Sequencer](/images/gce_genseq.png)

------

![An experience chicken working](/images/gce_excham.png)

------

By default, a chicken will display its resource in a custom name after it has
been passed through a Genetic Sequencer. If the chicken already has a custom
name, then the resource will be appended to the end of that name. This feature
can be turned off globally in the plugin's `config.yml`.

![A nether quartz chicken named Crystal](/images/gce_names.png)

## Thanks

None of this would be possible if it weren't for TheBusyBiscuit's hard work on
Slimefun, as well as the tremendously helpful Developer's Guide. Furthermore, a 
significant portion of this addon uses similar or identical code from
MobCapturer, and this project would not have gotten off the ground without it.

For this reborn version, I (ybw0014) would like to thank the following people:

- @kii-chan-reloaded (for the original plugin)
- @xMikux (Traditional Chinese translation)
- @LobbyTech-MC (Simplified Chinese translation)
- @zimzaza4 (Make it compatible with Slimefun RC-27+)
- @CrispyXYZ (current maintainer for Simplified Chinese version, added Ultimate Exciation Chamber)

## Issues/Requests

If you have any issues or feature requests, feel free to open an issue about it.
If you're experiencing bugs, please provide any relevent server logs in your issue.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
