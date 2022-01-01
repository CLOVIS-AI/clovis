@file:Suppress("unused") // Most colors in this file will never be used in code

package clovis.ui.theme.color

/*
 * Standard color selection.
 *
 * Inspired by the TailwindCSS color selection, available under the MIT licence.
 * https://tailwindcss.com/docs/customizing-colors
 * https://github.com/tailwindlabs/tailwindcss/blob/b8cda161dd0993083dcef1e2a03988c70be0ce93/src/public/colors.js
 */

@Suppress("SpellCheckingInspection") // IDEA doesn't like hexadecimal notation
private val standardColors = mapOf(
	"slate50" to "f8fafc",
	"slate100" to "f1f5f9",
	"slate200" to "e2e8f0",
	"slate300" to "cbd5e1",
	"slate400" to "94a3b8",
	"slate500" to "64748b",
	"slate600" to "475569",
	"slate700" to "334155",
	"slate800" to "1e293b",
	"slate900" to "0f172a",
	"gray50" to "f9fafb",
	"gray100" to "f3f4f6",
	"gray200" to "e5e7eb",
	"gray300" to "d1d5db",
	"gray400" to "9ca3af",
	"gray500" to "6b7280",
	"gray600" to "4b5563",
	"gray700" to "374151",
	"gray800" to "1f2937",
	"gray900" to "111827",
	"zinc50" to "fafafa",
	"zinc100" to "f4f4f5",
	"zinc200" to "e4e4e7",
	"zinc300" to "d4d4d8",
	"zinc400" to "a1a1aa",
	"zinc500" to "71717a",
	"zinc600" to "52525b",
	"zinc700" to "3f3f46",
	"zinc800" to "27272a",
	"zinc900" to "18181b",
	"neutral50" to "fafafa",
	"neutral100" to "f5f5f5",
	"neutral200" to "e5e5e5",
	"neutral300" to "d4d4d4",
	"neutral400" to "a3a3a3",
	"neutral500" to "737373",
	"neutral600" to "525252",
	"neutral700" to "404040",
	"neutral800" to "262626",
	"neutral900" to "171717",
	"stone50" to "fafaf9",
	"stone100" to "f5f5f4",
	"stone200" to "e7e5e4",
	"stone300" to "d6d3d1",
	"stone400" to "a8a29e",
	"stone500" to "78716c",
	"stone600" to "57534e",
	"stone700" to "44403c",
	"stone800" to "292524",
	"stone900" to "1c1917",
	"red50" to "fef2f2",
	"red100" to "fee2e2",
	"red200" to "fecaca",
	"red300" to "fca5a5",
	"red400" to "f87171",
	"red500" to "ef4444",
	"red600" to "dc2626",
	"red700" to "b91c1c",
	"red800" to "991b1b",
	"red900" to "7f1d1d",
	"orange50" to "fff7ed",
	"orange100" to "ffedd5",
	"orange200" to "fed7aa",
	"orange300" to "fdba74",
	"orange400" to "fb923c",
	"orange500" to "f97316",
	"orange600" to "ea580c",
	"orange700" to "c2410c",
	"orange800" to "9a3412",
	"orange900" to "7c2d12",
	"amber50" to "fffbeb",
	"amber100" to "fef3c7",
	"amber200" to "fde68a",
	"amber300" to "fcd34d",
	"amber400" to "fbbf24",
	"amber500" to "f59e0b",
	"amber600" to "d97706",
	"amber700" to "b45309",
	"amber800" to "92400e",
	"amber900" to "78350f",
	"yellow50" to "fefce8",
	"yellow100" to "fef9c3",
	"yellow200" to "fef08a",
	"yellow300" to "fde047",
	"yellow400" to "facc15",
	"yellow500" to "eab308",
	"yellow600" to "ca8a04",
	"yellow700" to "a16207",
	"yellow800" to "854d0e",
	"yellow900" to "713f12",
	"lime50" to "f7fee7",
	"lime100" to "ecfccb",
	"lime200" to "d9f99d",
	"lime300" to "bef264",
	"lime400" to "a3e635",
	"lime500" to "84cc16",
	"lime600" to "65a30d",
	"lime700" to "4d7c0f",
	"lime800" to "3f6212",
	"lime900" to "365314",
	"green50" to "f0fdf4",
	"green100" to "dcfce7",
	"green200" to "bbf7d0",
	"green300" to "86efac",
	"green400" to "4ade80",
	"green500" to "22c55e",
	"green600" to "16a34a",
	"green700" to "15803d",
	"green800" to "166534",
	"green900" to "14532d",
	"emerald50" to "ecfdf5",
	"emerald100" to "d1fae5",
	"emerald200" to "a7f3d0",
	"emerald300" to "6ee7b7",
	"emerald400" to "34d399",
	"emerald500" to "10b981",
	"emerald600" to "059669",
	"emerald700" to "047857",
	"emerald800" to "065f46",
	"emerald900" to "064e3b",
	"teal50" to "f0fdfa",
	"teal100" to "ccfbf1",
	"teal200" to "99f6e4",
	"teal300" to "5eead4",
	"teal400" to "2dd4bf",
	"teal500" to "14b8a6",
	"teal600" to "0d9488",
	"teal700" to "0f766e",
	"teal800" to "115e59",
	"teal900" to "134e4a",
	"cyan50" to "ecfeff",
	"cyan100" to "cffafe",
	"cyan200" to "a5f3fc",
	"cyan300" to "67e8f9",
	"cyan400" to "22d3ee",
	"cyan500" to "06b6d4",
	"cyan600" to "0891b2",
	"cyan700" to "0e7490",
	"cyan800" to "155e75",
	"cyan900" to "164e63",
	"sky50" to "f0f9ff",
	"sky100" to "e0f2fe",
	"sky200" to "bae6fd",
	"sky300" to "7dd3fc",
	"sky400" to "38bdf8",
	"sky500" to "0ea5e9",
	"sky600" to "0284c7",
	"sky700" to "0369a1",
	"sky800" to "075985",
	"sky900" to "0c4a6e",
	"blue50" to "eff6ff",
	"blue100" to "dbeafe",
	"blue200" to "bfdbfe",
	"blue300" to "93c5fd",
	"blue400" to "60a5fa",
	"blue500" to "3b82f6",
	"blue600" to "2563eb",
	"blue700" to "1d4ed8",
	"blue800" to "1e40af",
	"blue900" to "1e3a8a",
	"indigo50" to "eef2ff",
	"indigo100" to "e0e7ff",
	"indigo200" to "c7d2fe",
	"indigo300" to "a5b4fc",
	"indigo400" to "818cf8",
	"indigo500" to "6366f1",
	"indigo600" to "4f46e5",
	"indigo700" to "4338ca",
	"indigo800" to "3730a3",
	"indigo900" to "312e81",
	"violet50" to "f5f3ff",
	"violet100" to "ede9fe",
	"violet200" to "ddd6fe",
	"violet300" to "c4b5fd",
	"violet400" to "a78bfa",
	"violet500" to "8b5cf6",
	"violet600" to "7c3aed",
	"violet700" to "6d28d9",
	"violet800" to "5b21b6",
	"violet900" to "4c1d95",
	"purple50" to "faf5ff",
	"purple100" to "f3e8ff",
	"purple200" to "e9d5ff",
	"purple300" to "d8b4fe",
	"purple400" to "c084fc",
	"purple500" to "a855f7",
	"purple600" to "9333ea",
	"purple700" to "7e22ce",
	"purple800" to "6b21a8",
	"purple900" to "581c87",
	"fuchsia50" to "fdf4ff",
	"fuchsia100" to "fae8ff",
	"fuchsia200" to "f5d0fe",
	"fuchsia300" to "f0abfc",
	"fuchsia400" to "e879f9",
	"fuchsia500" to "d946ef",
	"fuchsia600" to "c026d3",
	"fuchsia700" to "a21caf",
	"fuchsia800" to "86198f",
	"fuchsia900" to "701a75",
	"pink50" to "fdf2f8",
	"pink100" to "fce7f3",
	"pink200" to "fbcfe8",
	"pink300" to "f9a8d4",
	"pink400" to "f472b6",
	"pink500" to "ec4899",
	"pink600" to "db2777",
	"pink700" to "be185d",
	"pink800" to "9d174d",
	"pink900" to "831843",
	"rose50" to "fff1f2",
	"rose100" to "ffe4e6",
	"rose200" to "fecdd3",
	"rose300" to "fda4af",
	"rose400" to "fb7185",
	"rose500" to "f43f5e",
	"rose600" to "e11d48",
	"rose700" to "be123c",
	"rose800" to "9f1239",
	"rose900" to "881337",
).mapValues { (_, value) -> Color.fromHex(value) }

val Color.Companion.allStandardColors get() = standardColors

val Color.Companion.slate50 by standardColors
val Color.Companion.slate100 by standardColors
val Color.Companion.slate200 by standardColors
val Color.Companion.slate300 by standardColors
val Color.Companion.slate400 by standardColors
val Color.Companion.slate500 by standardColors
val Color.Companion.slate600 by standardColors
val Color.Companion.slate700 by standardColors
val Color.Companion.slate800 by standardColors
val Color.Companion.slate900 by standardColors

val Color.Companion.gray50 by standardColors
val Color.Companion.gray100 by standardColors
val Color.Companion.gray200 by standardColors
val Color.Companion.gray300 by standardColors
val Color.Companion.gray400 by standardColors
val Color.Companion.gray500 by standardColors
val Color.Companion.gray600 by standardColors
val Color.Companion.gray700 by standardColors
val Color.Companion.gray800 by standardColors
val Color.Companion.gray900 by standardColors

val Color.Companion.zinc50 by standardColors
val Color.Companion.zinc100 by standardColors
val Color.Companion.zinc200 by standardColors
val Color.Companion.zinc300 by standardColors
val Color.Companion.zinc400 by standardColors
val Color.Companion.zinc500 by standardColors
val Color.Companion.zinc600 by standardColors
val Color.Companion.zinc700 by standardColors
val Color.Companion.zinc800 by standardColors
val Color.Companion.zinc900 by standardColors

val Color.Companion.neutral50 by standardColors
val Color.Companion.neutral100 by standardColors
val Color.Companion.neutral200 by standardColors
val Color.Companion.neutral300 by standardColors
val Color.Companion.neutral400 by standardColors
val Color.Companion.neutral500 by standardColors
val Color.Companion.neutral600 by standardColors
val Color.Companion.neutral700 by standardColors
val Color.Companion.neutral800 by standardColors
val Color.Companion.neutral900 by standardColors

val Color.Companion.stone50 by standardColors
val Color.Companion.stone100 by standardColors
val Color.Companion.stone200 by standardColors
val Color.Companion.stone300 by standardColors
val Color.Companion.stone400 by standardColors
val Color.Companion.stone500 by standardColors
val Color.Companion.stone600 by standardColors
val Color.Companion.stone700 by standardColors
val Color.Companion.stone800 by standardColors
val Color.Companion.stone900 by standardColors

val Color.Companion.red50 by standardColors
val Color.Companion.red100 by standardColors
val Color.Companion.red200 by standardColors
val Color.Companion.red300 by standardColors
val Color.Companion.red400 by standardColors
val Color.Companion.red500 by standardColors
val Color.Companion.red600 by standardColors
val Color.Companion.red700 by standardColors
val Color.Companion.red800 by standardColors
val Color.Companion.red900 by standardColors

val Color.Companion.orange50 by standardColors
val Color.Companion.orange100 by standardColors
val Color.Companion.orange200 by standardColors
val Color.Companion.orange300 by standardColors
val Color.Companion.orange400 by standardColors
val Color.Companion.orange500 by standardColors
val Color.Companion.orange600 by standardColors
val Color.Companion.orange700 by standardColors
val Color.Companion.orange800 by standardColors
val Color.Companion.orange900 by standardColors

val Color.Companion.amber50 by standardColors
val Color.Companion.amber100 by standardColors
val Color.Companion.amber200 by standardColors
val Color.Companion.amber300 by standardColors
val Color.Companion.amber400 by standardColors
val Color.Companion.amber500 by standardColors
val Color.Companion.amber600 by standardColors
val Color.Companion.amber700 by standardColors
val Color.Companion.amber800 by standardColors
val Color.Companion.amber900 by standardColors

val Color.Companion.yellow50 by standardColors
val Color.Companion.yellow100 by standardColors
val Color.Companion.yellow200 by standardColors
val Color.Companion.yellow300 by standardColors
val Color.Companion.yellow400 by standardColors
val Color.Companion.yellow500 by standardColors
val Color.Companion.yellow600 by standardColors
val Color.Companion.yellow700 by standardColors
val Color.Companion.yellow800 by standardColors
val Color.Companion.yellow900 by standardColors

val Color.Companion.lime50 by standardColors
val Color.Companion.lime100 by standardColors
val Color.Companion.lime200 by standardColors
val Color.Companion.lime300 by standardColors
val Color.Companion.lime400 by standardColors
val Color.Companion.lime500 by standardColors
val Color.Companion.lime600 by standardColors
val Color.Companion.lime700 by standardColors
val Color.Companion.lime800 by standardColors
val Color.Companion.lime900 by standardColors

val Color.Companion.green50 by standardColors
val Color.Companion.green100 by standardColors
val Color.Companion.green200 by standardColors
val Color.Companion.green300 by standardColors
val Color.Companion.green400 by standardColors
val Color.Companion.green500 by standardColors
val Color.Companion.green600 by standardColors
val Color.Companion.green700 by standardColors
val Color.Companion.green800 by standardColors
val Color.Companion.green900 by standardColors

val Color.Companion.emerald50 by standardColors
val Color.Companion.emerald100 by standardColors
val Color.Companion.emerald200 by standardColors
val Color.Companion.emerald300 by standardColors
val Color.Companion.emerald400 by standardColors
val Color.Companion.emerald500 by standardColors
val Color.Companion.emerald600 by standardColors
val Color.Companion.emerald700 by standardColors
val Color.Companion.emerald800 by standardColors
val Color.Companion.emerald900 by standardColors

val Color.Companion.teal50 by standardColors
val Color.Companion.teal100 by standardColors
val Color.Companion.teal200 by standardColors
val Color.Companion.teal300 by standardColors
val Color.Companion.teal400 by standardColors
val Color.Companion.teal500 by standardColors
val Color.Companion.teal600 by standardColors
val Color.Companion.teal700 by standardColors
val Color.Companion.teal800 by standardColors
val Color.Companion.teal900 by standardColors

val Color.Companion.cyan50 by standardColors
val Color.Companion.cyan100 by standardColors
val Color.Companion.cyan200 by standardColors
val Color.Companion.cyan300 by standardColors
val Color.Companion.cyan400 by standardColors
val Color.Companion.cyan500 by standardColors
val Color.Companion.cyan600 by standardColors
val Color.Companion.cyan700 by standardColors
val Color.Companion.cyan800 by standardColors
val Color.Companion.cyan900 by standardColors

val Color.Companion.sky50 by standardColors
val Color.Companion.sky100 by standardColors
val Color.Companion.sky200 by standardColors
val Color.Companion.sky300 by standardColors
val Color.Companion.sky400 by standardColors
val Color.Companion.sky500 by standardColors
val Color.Companion.sky600 by standardColors
val Color.Companion.sky700 by standardColors
val Color.Companion.sky800 by standardColors
val Color.Companion.sky900 by standardColors

val Color.Companion.blue50 by standardColors
val Color.Companion.blue100 by standardColors
val Color.Companion.blue200 by standardColors
val Color.Companion.blue300 by standardColors
val Color.Companion.blue400 by standardColors
val Color.Companion.blue500 by standardColors
val Color.Companion.blue600 by standardColors
val Color.Companion.blue700 by standardColors
val Color.Companion.blue800 by standardColors
val Color.Companion.blue900 by standardColors

val Color.Companion.indigo50 by standardColors
val Color.Companion.indigo100 by standardColors
val Color.Companion.indigo200 by standardColors
val Color.Companion.indigo300 by standardColors
val Color.Companion.indigo400 by standardColors
val Color.Companion.indigo500 by standardColors
val Color.Companion.indigo600 by standardColors
val Color.Companion.indigo700 by standardColors
val Color.Companion.indigo800 by standardColors
val Color.Companion.indigo900 by standardColors

val Color.Companion.violet50 by standardColors
val Color.Companion.violet100 by standardColors
val Color.Companion.violet200 by standardColors
val Color.Companion.violet300 by standardColors
val Color.Companion.violet400 by standardColors
val Color.Companion.violet500 by standardColors
val Color.Companion.violet600 by standardColors
val Color.Companion.violet700 by standardColors
val Color.Companion.violet800 by standardColors
val Color.Companion.violet900 by standardColors

val Color.Companion.purple50 by standardColors
val Color.Companion.purple100 by standardColors
val Color.Companion.purple200 by standardColors
val Color.Companion.purple300 by standardColors
val Color.Companion.purple400 by standardColors
val Color.Companion.purple500 by standardColors
val Color.Companion.purple600 by standardColors
val Color.Companion.purple700 by standardColors
val Color.Companion.purple800 by standardColors
val Color.Companion.purple900 by standardColors

val Color.Companion.fuchsia50 by standardColors
val Color.Companion.fuchsia100 by standardColors
val Color.Companion.fuchsia200 by standardColors
val Color.Companion.fuchsia300 by standardColors
val Color.Companion.fuchsia400 by standardColors
val Color.Companion.fuchsia500 by standardColors
val Color.Companion.fuchsia600 by standardColors
val Color.Companion.fuchsia700 by standardColors
val Color.Companion.fuchsia800 by standardColors
val Color.Companion.fuchsia900 by standardColors

val Color.Companion.pink50 by standardColors
val Color.Companion.pink100 by standardColors
val Color.Companion.pink200 by standardColors
val Color.Companion.pink300 by standardColors
val Color.Companion.pink400 by standardColors
val Color.Companion.pink500 by standardColors
val Color.Companion.pink600 by standardColors
val Color.Companion.pink700 by standardColors
val Color.Companion.pink800 by standardColors
val Color.Companion.pink900 by standardColors

val Color.Companion.rose50 by standardColors
val Color.Companion.rose100 by standardColors
val Color.Companion.rose200 by standardColors
val Color.Companion.rose300 by standardColors
val Color.Companion.rose400 by standardColors
val Color.Companion.rose500 by standardColors
val Color.Companion.rose600 by standardColors
val Color.Companion.rose700 by standardColors
val Color.Companion.rose800 by standardColors
val Color.Companion.rose900 by standardColors
