[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/VoperAD/SlimeFrame/blob/main/README.md)

<div align="center">
  <img src="https://github.com/VoperAD/SlimeFrame/blob/main/images/SFrame-Banner.png" alt="SlimeFrame-Banner_2" width="1280" height="500">
</div>

<details>
  <summary>Sumário</summary>
  <ol>
    <li><a href="#download">Download</a></li>
    <li><a href="#sobre-o-addon">Sobre o addon</a></li>
    <li><a href="#requisitos">Requisitos</a></li>
    <li><a href="#comandos">Comandos</a></li>
    <li>
      <a href="#o-que-o-addon-adiciona">O que o addon adiciona?</a>
      <ul>
        <li><a href="#novos-recursos">Novos Recursos</a></li>
        <li><a href="#relíquias">Relíquias</a></li>
        <li><a href="#geradores-acumulativos">Geradores Acumulativos</a></li>
        <li><a href="#auto-trader">Auto Trader</a></li>
      </ul>
    </li>
    <li><a href="#créditos">Créditos</a></li>
    <li><a href="#doações">Doações</a></li>
  </ol>
</details>

## Download

- <a href="https://github.com/VoperAD/SlimeFrame/releases/latest">Faça o download a partir de GitHub Releases</a> <b>(o arquivo JAR está disponível dentro de 'Assets')</b>
  <img src="https://github.com/VoperAD/SlimeFrame/assets/92862848/5dbd6cb1-41b6-4c99-bffd-53b7773ef740" width=600>


## Sobre o Addon

O SlimeFrame é um addon para o Slimefun inspirado em Warframe que adiciona novas máquinas, geradores de energia e muitos outros itens para tornar sua jogabilidade ainda mais divertida. Entre os itens adicionados estão o Auto Trader, capaz de fazer trocas automáticas com os aldeões, o Item Projector, que é basicamente um holograma para itens, geradores de concreto e lã e muito mais. Não tenha pressa para explorar todo o potencial desse addon!

## Requisitos

- Esse addon funciona com servidores **Spigot**/**Paper** assim como nos seus forks, como **Purpur**.
- **Java Version:** 16+
- **Minecraft Version:** 1.19+
- **Slimefun Version:** RC-34+

## Comandos

|            Comando            |          Permissão          |                       Descrição                       |
|:-----------------------------:|:---------------------------:|:-----------------------------------------------------:|
|               -               |     slimeframe.anyone.*     | Nodo de permissão para todos os comandos \'_anyone_\' |
|        /sframe relics         | slimeframe.anyone.inventory |           Abre seu inventário de relíquias            |
| /sframe refine \<refinement\> |  slimeframe.anyone.refine   |         Refina a relíquia que está na sua mão         |
|    /sframe traces [player]    |  slimeframe.anyone.traces   |   Mostra a quantidade de traços que um jogador tem    |
|               -               |     slimeframe.admin.*      | Nodo de permissão para todos os comandos \'_admin_\'  |
|   /sframe invsee \<player\>   |   slimeframe.admin.invsee   |    Abre o inventário de relíquias de outro jogador    |

## O que o addon adiciona?

### Novos Recursos

Há muitos recursos novos usados para criar as novas máquinas e geradores. Alguns deles são novos minérios que você pode obter minerando minérios do Minecraft usando uma picareta Nosam, uma picareta especial do addon. **Os blocos colocados por um jogador não dropam esses minérios.**

### Relíquias

As relíquias são itens usados para obter componentes Prime, que são componentes necessários para criar máquinas do tier Prime, as melhores máquinas do addon. Cada relíquia tem 3 recompensas de bronze, 2 recompensas de prata e 1 recompensa de ouro (comum, incomum e rara). Há quatro tipos de relíquias:

- Lith: Obtida a cada 50 peixes capturados.
- Meso: Obtida a cada 750 entidades mortas.
- Neo: Obtida a cada 10.000 blocos quebrados.
- Axi: Obtido a cada 750 blocos colocados.

> **Nota:** Essas quantidades podem ser modificadas no arquivo de configuração (config.yml).

#### Como abrir uma Relíquia?

As relíquias têm dois atributos mostrados na sua descrição/lore: Reactants e Refinement. Para abrir uma relíquia, você deve preenchê-la com 10 reactants. Para obter reactants, coloque **UMA** relíquia em sua mão livre e mate Endermen até que ela fique cheia. Cada Enderman morto tem 25% de chance de lhe dar um reactant. Quando chegar a 10 reactants, basta clicar com o botão direito do mouse com a relíquia na mão e ela se abrirá, dando-lhe uma recompensa.

> ***Jogadores Bedrock:*** Se você for um jogador Bedrock, deverá colocar a relíquia no primeiro slot da esquerda para a direita (_é experimental e ainda está sendo testado, portanto, se tiver algum problema, informe-o_).

#### Refinamento de Relíquias

As relíquias podem ser refinadas para aumentar as chances de obter recompensas mais raras. 
Para refinar uma relíquia, use o comando ```/sframe refine <refinement>```. 
Você precisará de **Void Traces**, que são obtidos sempre que uma relíquia é aberta (você recebe uma quantidade aleatória de void traces entre 1 e 20). Para verificar quantos void traces você tem, use o comando ```/sframe traces```. Há quatro refinamentos:

- INTACT: O refinamento padrão.
- EXCEPTIONAL: Custa 25 void traces.
- FLAWLESS: Custa 50 void traces.
- RADIANT: Custa 100 void traces.

### Geradores acumulativos

Um novo tipo de gerador de energia que verifica se os blocos ao lado, acima ou abaixo dele também são geradores acumulativos. Para cada Gerador acumulativo detectado, uma quantidade de energia, indicada por um novo atributo na descrição do gerador chamado **Bonus Energy**, será adicionada à energia total gerada.
> **Nota:** Os blocos diagonais não são contados. Isso significa que o gerador de energia acumulativo verificará um total de 6 blocos (1 para cada lado, 1 acima e 1 abaixo).

### Auto Trader

Uma nova máquina que pode fazer trocas automaticamente com aldeões. Para ela funcionar, você precisará de um ***Merchant Soul Contract***. Quando tiver um contrato, escolha um aldeão e clique com o botão direito do mouse nele com o contrato na mão. Isso matará o aldeão e preencherá o contrato com suas trocas. Depois disso, coloque o contrato na Auto Trader e selecione uma das trocas.

> **Automatizando o farm dos novos minérios:** essa máquina pode ser usada para produzir automaticamente os novos minérios que você obtém usando a picareta Nosam. Toda vez que um aldeão recebe uma nova troca, há uma pequena chance de que ela ofereça um dos novos recursos adicionais. Nesse caso, use o Merchant Soul Contract novamente e use o Auto Trader com esse novo contrato.

## Créditos

Obrigado a <a href="https://minecraft-heads.com/">Minecraft Heads</a> pelas texturas de heads usadas no addon.

[![](https://minecraft-heads.com/images/banners/minecraft-heads_fullbanner_468x60.png)](https://minecraft-heads.com/)

Também agradeço a todas as pessoas que me ajudaram no canal de programação no servidor de Discord do Slimefun.
Obrigado ao servidor **AbsolutGG** pelos testes realizados na fase inicial do addon.

## Doações

Se quiser apoiar o projeto e fazer o meu dia, você pode fazer uma doação em <a href="https://ko-fi.com/voper">Ko-Fi</a> 🙂

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
