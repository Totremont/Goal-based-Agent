<div align="center">
  <img src="/gui-demo.png"/>
  <p>Game GUI made with Java Swing by <a href="https://github.com/JeremiasMVL">JeremiasMVL</a></p>
</div>

# About 

This project implements a goal based agent capable of playing a simplified version of the videogame [Among Us](https://en.wikipedia.org/wiki/Among_Us). It makes use of different search tree strategies to find the solution.

The Agent takes on the **Impostor** role and will try to achieve his goal by completing the following tasks.

1. Killing all the Crewmates.
2. Sabotaging all the target rooms.

In order to complete those tasks, the agent can:

1. Move to an adjacent room.
2. Kill a crewmate.
3. Sabotage a room.

The game map consists on spaceship rooms connected by corridors. Vents are not included.

The impostor has a built-in compass and sensors to locate and scan rooms.
Crewmates are entities programmed to move randomly between rooms during the game session.

When the game begins, crewmates are randomly scattered around the map. Then, the agent is given an initial amount of energy that decreases everytime it executes an action. The game is lost if the agent's energy drops to zero before completing the tasks.

Different search trees were implemented on multiple scenarios with interesting results. As a tl;dr:

- **Uninformed search**.
  - `Breadth first` finds the solution on smaller scenarios ( <6 crewmates). It has a high memory consumption that renders it impractical in most cases.
  - `Depth first` can usually find the next action on bigger games but consumes a lot of energy.

- **Informed search**. The strategies reported are suitable for games of intermediate size (4-12 crewmates) with a fixed set of rules.
  - `Greedy` gave the best results in terms of game size, memory usage and decision time (maximum time spent searching for the next action).
  - `A-Star` results were slighly better than Greedy but pretty much the same. 


The program was written in Java using the [FAIA Framework](https://jidis.frc.utn.edu.ar/papers/c5dca9488dfd645c135a16c9ccf4.pdf).

A detail explanation of the agent model, implementation and game results can be found on `/Informe.pdf` (Spanish).

This project was made as part of a course on Artificial Intelligence for my undergrad degree on [Ingeniería en Sistemas de Información](https://utn.edu.ar/es/federacion-universitaria-tecnologica/feria-de-carreras/sistemas-de-informacion) (Information Systems Engineering).
