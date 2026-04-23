$repoId = "R_kgDOSKubnA"
$categories = @(
    @{ name = "Slimefun & Addons"; emoji = "🧪"; description = "Discusiones técnicas sobre el ecosistema Slimefun." },
    @{ name = "Minecraft (Servers & Tech)"; emoji = "🎮"; description = "Configuración de servidores, plugins y tecnología MC." },
    @{ name = "Java (Development & APIs)"; emoji = "☕"; description = "Desarrollo en Java, Maven y APIs personalizadas." },
    @{ name = "Python & Automation"; emoji = "🐍"; description = "Scripts, automatización e IA con Python." },
    @{ name = "Proyectos de Jack"; emoji = "⭐"; description = "Actualizaciones y feedback sobre los proyectos de Jack." },
    @{ name = "Proyectos de Chagui"; emoji = "🛠️"; description = "Actualizaciones y feedback sobre los proyectos de Chagui." }
)

foreach ($cat in $categories) {
    $query = @"
mutation {
  createDiscussionCategory(input: {
    repositoryId: \"$repoId\",
    name: \"$($cat.name)\",
    emoji: \"$($cat.emoji)\",
    description: \"$($cat.description)\"
  }) {
    category {
      name
    }
  }
}
"@
    gh api graphql -f query="$query"
}
