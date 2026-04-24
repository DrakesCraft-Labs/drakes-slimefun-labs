# WorldEditSlimefun

This plugin only works on Paper and it's forks.

WorldEditSlimefun allows people to paste in Slimefun blocks.
This can especially be helpful for addon creators, server owners and people who test plugins.
This can be helpful with testing how optimized the blocks are or if they would cause any issues in mass usage.

## Commands
- `/wesf wand` 
  - This command gives a player the selection wand
- `/wesf pos1`
  - This command sets position 1.
- `/wesf pos2` 
  - This command sets position 2.
- `/wesf paste <slimefun_block> [flags...]` 
  - This command pastes the block specified in the area selected.
  - `<slimefun_block>`, a string id, it can be any slimefun or slimefun addon block
  - `[energy]`, a boolean, charges the pasted blocks to max integer so they can process pseudo-infinitely (if possible)
  - `[inputs]`, a string array, places these items in the blocks input slots (if possible)
  - `[refill_inputs_task]`, a boolean, if the inputs should be refilled every slimefun tick (if possible)
    - Uses the inputs provided by the `[inputs]` flag, if none are provided, the task is not scheduled
    - Lasts for a default of 5 minutes, however can be overridden by the `[task_timeout]` flag
    - If a block is broken it is removed from the task, if all blocks are broken, the task ends automatically
  - `[void_outputs_task]`, a boolean, if the outputs should be voided every slimefun tick (if possible)
    - Lasts for a default of 5 minutes, however can be overridden by the `[task_timeout]` flag
    - If a block is broken it is removed from the task, if all blocks are broken, the task ends automatically
  - `[task_timeout]`, a string (`30s` `5m` `1h`), the amount of time any tasks should run for (if possible)
- `/wesf clear [call_event]`
  - This command clears the blocks you have selected with the position commands. 
  - `[call_event]`, a boolean, if a `BlockBreakEvent` should be used to trigger item handlers. (defaults to `false`)

## Download
You can find the download of this addon in on [Blob Builds](https://blob.build/project/WorldEditSlimefun).

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
