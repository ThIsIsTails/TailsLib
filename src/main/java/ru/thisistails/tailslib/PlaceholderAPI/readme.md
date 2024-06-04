# Placeholders

## %geteffectfromplayer_(index)%

Use it for online players only.
Need for other placeholders. (Effect IDs to be exact)

Params:
* index: integer - effect index in list.

Returns:
* ID of effect or null if not found.

## %effectsraw%

Use it for online players only.

Returns:
* Raw effects. (Example: `blanEffect otherBlankEffect`)

## %effectsformatted_(max)%

Use it for online players only.

Params:
* max: integer - The enumeration of what effect should be followed by three dots?

Returns:
* Formatted effects. (Example: `blanEffect1, blankEffect2, blankEffect3` or `blankEffect1, blankEffect2...` if param is used.)

## %effectduration_(effectID)%

Use it for online players only

Params:
* effectID: string - Custom effect id (Should be applied on player)

Returns:
* null if effect not found or duration of effect in seconds.

