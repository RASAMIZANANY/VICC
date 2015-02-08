# Notes about the project

## The team

- BEN ABDELJELIL Mohamed: m.abdeljelil@outlook.com
- RASAMIZANANY Sam: rskkya@gmail.com

## Comments

What did you do?

We did antiAffinity, balance, noViolation and statEnergy

What failed? What succeeded?

All succeeded except statEnergy

How did you do? Why?

-antiAffinity:
-balance: Les hôtes par mips disponible et on ajoute une vm dans celui qui a le plus grand nombre de mips disponible.
-noViolation: pour ajouter une vm il faut qu'elle soit adaptée à l'hôte, au final toutes les vms qui ont été ajoutée à l'hôte
ne créeront aucune pénalité. (fonction issueTable).
-statEnergy: failed.

Interesting project or not?

Oui ce projet est intéressant car ça nous a permi de voir les contraintes auxquelles sont soumis les providers tout en essayant de maximiser ses bénéfices tout en essayant de satisafaire les demandes du client. Pour cela plusieurs stratégies ont été mises en oeuvre dans le projet avec chacun leur avantage/inconvénient.

Support for Highly-Available applications

2.The complexity of the algorithm is O(h²), beacause there are 2 loops for.
3. Pour garder des machines virtuelles opérationnelles avec les mêmes affinités sur différents hôtes, dans le cas où une machine virtuelle avec une affinité spécifique tomberait en panne sur un hôte.

Balance the load

1. The complexity of the algorithm is O(h), we have only one loop for a list.
2. For the measurement of balancing whe chose the number of hosts which work and we can see with balancing load that all of hosts are used to balance the load.

Get rid of SLA violations

The complexity of the algorithm is O(h).

Energy-efficient schedulers

static version

The complexity of the algorithm is O(h)
