
:-[pokemon_data].


% find_pokemon_evolution(+PokemonLevel, +Pokemon, -EvolvedPokemon)
% This predicate is to find the evolved version of Pokemon according to its level. First line is recursive, because if Pokemon has enough levels, it should evolve as 
% much as possible. However, if Pokemon finally reaches a higher required level for evolution then it has, the recursion should stop and Pokemon should
% be equal to the current EvolvedPokemon argument. 

find_pokemon_evolution(X, Pokemon, EvolvedPokemon):-pokemon_evolution(Pokemon, A, Y), X>=Y, find_pokemon_evolution(X, A, EvolvedPokemon), !.
find_pokemon_evolution(_, Pokemon, Pokemon).



% pokemon_level_stats(+PokemonLevel, ?Pokemon, -PokemonHP, -PokemonAttack, -PokemonDefense)
% This predicate evaluates the statistics of Pokemon a according to its level. It takes its base statistics from pokemon_stats and does the calculation according
% to the formula. If the Pokemon is unknown, it calculates the statistics of all Pokemons.

pokemon_level_stats(PokemonLevel, Pokemon, PokemonHp, PokemonAttack, PokemonDefense):-pokemon_stats(Pokemon, _, HealthPoint, Attack, Defense), 
                                                                                      PokemonHp is HealthPoint+PokemonLevel*2, 
																					  PokemonAttack is Attack+PokemonLevel, 
																					  PokemonDefense is Defense+PokemonLevel.

																					  
																					  
% ---a helper predicate--- defenderTypeIndex(+DefenderType, -Index) 
% This predicate is to find the index of a Pokemon type from the list in pokemon_types, in order to use it while finding or calculating the multipliers.

defenderTypeIndex(DefenderType, Index):-pokemon_types(PokemonList), nth0(Index, PokemonList, DefenderType), !.
																					  
																					  
																					  
% single_type_multiplier(?AttackerType, +DefenderType, ?Multiplier)
% This predicate is to find single-type multiplier or types that achieve a given multiplier. If the attacker type is known and multiplier is unknown,
% it finds the list of the attacker in type_chart_attack, goes to correct index and returns its value. If the attacker type is unknown and multiplier is known, 
% it finds the attacker type which has the given multiplier according to the defenderType. If both the attacker type and multiplier are unknown, it finds the
% multipliers of all attacker types according to the given defenderType. 
  
single_type_multiplier(AttackerT, DefenderType, Multiplier):-type_chart_attack(AttackerT, TypeMultipliers), 
														     defenderTypeIndex(DefenderType, Index), 
														     nth0(Index, TypeMultipliers, Multiplier).

														  
														  
% type_multiplier(?AttackerType, +DefenderTypeList, ?Multiplier)
% This predicate is to find double-type multiplier or types that achieve a given multiplier. It recursively finds all single-type multipliers according to 
% DefenderTypeList and multiply them until the list finishes. If the attacker type is known and multiplier is unknown, it finds all its single-type multipliers
% and multiply them. If both the attacker type and multiplier are unknown, it calculates the multipliers for all attacker types. If the attacker type is unknown and 
% multiplier is known, it finds the attacker type whish has the given multiplier according to the defenderTypeList.

type_multiplier(_, [], CurrentMultiplier, CurrentMultiplier).
type_multiplier(AttackerType,[DefenderHead|DefenderTail], CurrentMultiplier, Multiplier):-single_type_multiplier(AttackerType, DefenderHead, HeadMultiplier), 
                                                                                          NewCurrentMultiplier is CurrentMultiplier*HeadMultiplier, 
																						  type_multiplier(AttackerType, DefenderTail, NewCurrentMultiplier, Multiplier).  
type_multiplier(AttackerType,DefenderTypeList, Multiplier):- type_multiplier(AttackerType, DefenderTypeList, 1, Multiplier).



% ---a helper predicate--- greater(+A, +B, -Max)
% This predicate is to find which one of the two elements is greater than the other. It is used in pokemon_type_multipliers while comparing the 
% values and finding the greater one.

greater(A, B, Max):- A>=B, Max=A.
greater(A, B, Max):- A<B, Max=B.


% ---a helper predicate--- compare(+AttackerList, +DefendList, +CurrentMultiplier, -Multiplier)
% This predicate is used in pokemon_type_multiplier predicate. It calculates all multipliers of the types in AttackerList according to the 
% DefendList and make the maximum multiplier equal to the Multiplier. AttackerList can be empty or non-empty, and it can be recursive or non-recursive. 
% The complete version of the compare predicate is written in pokemon_type_multiplier predicate.



% pokemon_type_multiplier(?AttackerPokemon, ?DefenderPokemon, ?Multiplier)
% This predicate is to find the maximum multiplier that can a Pokemon have. First, it gets the type lists of AttackerPokemon and DefenderPokemon.
% Then, with the help of compare and greater predicates, it calculates all multipliers of AttackerPokemon according to the AttackList and DefendList 
% recursively until the AttackList finishes and returns the maximum one. After the list finishes, it makes the CurrentMultiplier(maximum one) 
% equal to the Multiplier.

compare([], _, CurrentMultiplier, CurrentMultiplier).
compare([AttackerHead|AttackerTail], DefendList, CurrentMultiplier, Multiplier):-type_multiplier(AttackerHead, DefendList, NextMultiplier), 
                                                                                 greater(CurrentMultiplier, NextMultiplier, MaxMultiplier), 
																				 compare(AttackerTail, DefendList, MaxMultiplier, Multiplier).
pokemon_type_multiplier(AttackerPokemon, DefenderPokemon, CurrentMultiplier, Multiplier):-pokemon_stats(AttackerPokemon, AttackList, _, _, _), 
																						  pokemon_stats(DefenderPokemon, DefendList, _, _, _), 
																						  compare(AttackList, DefendList, CurrentMultiplier, Multiplier).  
pokemon_type_multiplier(AttackerPokemon, DefenderPokemon, Multiplier):-pokemon_type_multiplier(AttackerPokemon, DefenderPokemon, 0, Multiplier).



% pokemon_attack(+AttackerPokemon, +AttackerPokemonLevel, +DefenderPokemon, +DefenderPokemonLevel, -Damage)
% This predicate is to find the damage point when the AttackerPokemon attacks to the DefenderPokemon. With the help of pokemon_level_stats
% and pokemon_type_multiplier, it gets AttackerPokemonAttack, AttackerPokemonDefense and TypeMultiplier which are necessary for calculating 
% the damage dealt.
  
pokemon_attack(AttackerPokemon, AttackerPokemonLevel, DefenderPokemon, DefenderPokemonLevel, Damage):-pokemon_level_stats(AttackerPokemonLevel, AttackerPokemon, _, AttackerPokemonAttack, _), 
                                                                                                      pokemon_level_stats(DefenderPokemonLevel, DefenderPokemon, _, _, DefenderPokemonDefense), 
																									  pokemon_type_multiplier(AttackerPokemon, DefenderPokemon, Multiplier), 
																									  Damage is (0.5*AttackerPokemonLevel* (AttackerPokemonAttack/ DefenderPokemonDefense)*Multiplier)+1, !.

																									  

% ---a helper predicate--- decideFinish(+CurrentPokemon1Hp, +CurrentPokemon2Hp, +Pokemon1, +Pokemon1Level, +Pokemon2, +Pokemon2Level, -Pokemon1Hp, -Pokemon2Hp, +Rnew, -Rounds, +Damageto1, +Damageto2)
% This predicate is to decide whether the fight in pokemon_fight predicate should finish or not. It decides it by checking whether CurrentPokemon1Hp or CurrentPokemon2Hp
% is <=0. If so, the fight finishes and it returns the values of Pokemon1Hp, Pokemon2Hp and Rounds. If not, it calls pokemon_fight predicate and the fight continues
% with currentPokemonHps. The complete version of this predicate is written in pokemon_fight predicate.


																									  
% pokemon_fight(+Pokemon1, +Pokemon1Level, +Pokemon2, +Pokemon2Level, -Pokemon1Hp, -Pokemon2Hp, -Rounds)
% This predicate is to simulate a fight between two Pokemons. To achieve it, this predicate is runned recursively until a Pokemon's Hp reach or drop below zero.
% With the help of pokemon_level_stats, it gets initial Hp values and from pokemon_attack it gets the damage points. The fight runs recursively. At each call,
% Hps of Pokemons get their new values(by subtracting the damages), round counter is increased by one and it goes to the decideFinish to decide whether the fight 
% should finish or not. If it should finish, Pokemon1Hp, Pokemon2Hp and Rounds get their values. If not, fight countinues with the same recursion. 

decideFinish(CurrentPokemon1Hp, CurrentPokemon2Hp, Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Pokemon1Hp, Pokemon2Hp, Rnew, Rounds, Damageto1, Damageto2):- CurrentPokemon1Hp>0, CurrentPokemon2Hp>0, pokemon_fight(Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Pokemon1Hp, Pokemon2Hp, Rnew, Rounds, Damageto1, Damageto2, CurrentPokemon1Hp, CurrentPokemon2Hp).
decideFinish(CurrentPokemon1Hp, CurrentPokemon2Hp, _, _, _, _, Pokemon1Hp, Pokemon2Hp, Rnew, Rounds, _, _):- CurrentPokemon1Hp=<0, Pokemon1Hp is CurrentPokemon1Hp, Pokemon2Hp is CurrentPokemon2Hp, Rounds is Rnew.
decideFinish(CurrentPokemon1Hp, CurrentPokemon2Hp, _, _, _, _, Pokemon1Hp, Pokemon2Hp, Rnew, Rounds, _, _):- CurrentPokemon2Hp=<0, Pokemon1Hp is CurrentPokemon1Hp, Pokemon2Hp is CurrentPokemon2Hp, Rounds is Rnew.

pokemon_fight(Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Pokemon1Hp, Pokemon2Hp, R, Rounds, Damageto1, Damageto2, OldPokemon1Hp, OldPokemon2Hp):- CurrentPokemon1Hp is OldPokemon1Hp-Damageto1,
																																					     CurrentPokemon2Hp is OldPokemon2Hp-Damageto2,
																																					     Rnew is R+1, 
																																					     decideFinish(CurrentPokemon1Hp, CurrentPokemon2Hp, Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Pokemon1Hp, Pokemon2Hp, Rnew, Rounds, Damageto1, Damageto2).

pokemon_fight(Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Pokemon1Hp, Pokemon2Hp, Rounds):-pokemon_level_stats(Pokemon1Level, Pokemon1, OldPokemon1Hp, _, _), 
                                                                                                 pokemon_level_stats(Pokemon2Level, Pokemon2, OldPokemon2Hp, _, _),
																								 pokemon_attack(Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Damageto2), 
																								 pokemon_attack(Pokemon2, Pokemon2Level, Pokemon1, Pokemon1Level, Damageto1),
																								 pokemon_fight(Pokemon1, Pokemon1Level, Pokemon2, Pokemon2Level, Pokemon1Hp, Pokemon2Hp, 0, Rounds, Damageto1, Damageto2, OldPokemon1Hp, OldPokemon2Hp), !.

																								 
																								 
% ---a helper predicate--- winner(+PokemonTrainer1, +PokemonTrainer2, +Pokemon1Hp, +Pokemon2Hp, -WinnerTrainerList, +OldList)
% This predicate is used in pokemon_tournament. By comparing the Hps of Pokemons, it decides which Pokemon should be added to the winner list. If there's a tie,
% Pokemon1 is the winner. After adding, it calls tournament to make the tournament continue. The complete version of this predicate is written in pokemon_tournament predicate. 



% ---a helper predicate--- tournament(+PokemonTrainer1, +PokemonTeam1, +PokemonTeam1Levels, +PokemonTrainer2, +PokemonTeam2, +PokemonTeam2Levels, -WinnerTrainerList, +OldList)
% This predicate is used in pokemon_tournament. Firstly, it makes Pokemons to evolve if they have enough levels. Then, it simulates a fight with the help 
% of pokemon_fight, and calls the winner after each fights in order to add all winners to the OldList. Winner and tournament call each other until the
% PokemonTeam1 and PokemonTeam2 lists finish. After they finish, it makes the WinnerTrainerList equal to the OldList(current winnerlist). 
% The complete version of this predicate is written in pokemon_tournament predicate.



% pokemon_tournament(+PokemonTrainer1, +PokemonTrainer2, -WinnerTrainerList)
% This predicate simulates a tournament between two Pokemon teams. With the help of tournament and winner predicates, the tournament continues until
% both teams finish. It calls pokemon_trainer to get the Pokemon teams and their level lists, and calls the tournament predicate to start the tournament.
 
winner(PokemonTrainer1, PokemonTrainer2, Pokemon1Hp, Pokemon2Hp, Tail1, LevelTail1, Tail2, LevelTail2,  WinnerTrainerList, OldList):- Pokemon1Hp>Pokemon2Hp, 
                                                                                                                                      append(OldList, [PokemonTrainer1],  NewList), 
																																	  tournament(PokemonTrainer1, Tail1, LevelTail1, 
																																	  PokemonTrainer2, Tail2, LevelTail2, WinnerTrainerList, NewList).
winner(PokemonTrainer1, PokemonTrainer2, Pokemon1Hp, Pokemon2Hp, Tail1, LevelTail1, Tail2, LevelTail2, WinnerTrainerList, OldList):- Pokemon2Hp>Pokemon1Hp, 
                                                                                                                                     append(OldList, [PokemonTrainer2],  NewList),  
                                                                                                                                     tournament(PokemonTrainer1, Tail1, LevelTail1, PokemonTrainer2, Tail2, LevelTail2, WinnerTrainerList, NewList).
winner(PokemonTrainer1, PokemonTrainer2, Pokemon1Hp, Pokemon2Hp, Tail1, LevelTail1, Tail2, LevelTail2,  WinnerTrainerList, OldList):- Pokemon1Hp=Pokemon2Hp, 
                                                                                                                                      append(OldList, [PokemonTrainer1],  NewList), 
																																	  tournament(PokemonTrainer1, Tail1, LevelTail1, PokemonTrainer2, Tail2, LevelTail2, WinnerTrainerList, NewList).


tournament(_, [], [], _, [], [], A, A).
tournament(PokemonTrainer1, [Head1|Tail1], [LevelHead1|LevelTail1], PokemonTrainer2, [Head2|Tail2], [LevelHead2|LevelTail2], WinnerTrainerList, OldList):-find_pokemon_evolution(LevelHead1, Head1, EvolvedPokemon1),
																																					      find_pokemon_evolution(LevelHead2, Head2, EvolvedPokemon2),
																																					      pokemon_fight(EvolvedPokemon1, LevelHead1, EvolvedPokemon2, LevelHead2, Pokemon1Hp, Pokemon2Hp, _), 
																																					      winner(PokemonTrainer1, PokemonTrainer2, Pokemon1Hp, Pokemon2Hp, Tail1, LevelTail1, Tail2, LevelTail2, WinnerTrainerList, OldList), !.

pokemon_tournament(PokemonTrainer1, PokemonTrainer2, WinnerTrainerList):-pokemon_trainer(PokemonTrainer1, PokemonTeam1, PokemonTeam1Levels), 
																		 pokemon_trainer(PokemonTrainer2, PokemonTeam2, PokemonTeam2Levels),
																		 tournament(PokemonTrainer1, PokemonTeam1, PokemonTeam1Levels, PokemonTrainer2, PokemonTeam2, PokemonTeam2Levels, WinnerTrainerList, _).

																		 
% ---a helper predicate--- notinst(+Var)
% This predicate is used in best_pokemon predicate. It is true if the variable is not initialized.

notinst(Var):-  \+(\+(Var=0)), \+(\+(Var=1)).



% ---a helper predicate--- inst(+Var)	
% This predicate is used in best_pokemon predicate. It is true if the variable is already initialized.

inst(Var):- (\+(Var=0)), (\+(Var=1)). 


% ---a helper predicate--- decide_best(+EnemyPokemon, +Pokemon2, +LevelCap, +Pokemon2Hp, CurBest, +CurHp, ?A, ?B, Cnew, Inew, List, -BestPokemon, -RemainingHp, N)	
% This predicate is used in best_pokemon predicate. After each fight, it takes the Hp and compares it with the current greatest Hp. If this Hp is greater than the
% current one, it replaces it with the new one (it also replaces the Pokemon) and continues with the new fights by calling best_pokemon.
																	 
% best_pokemon(+EnemyPokemon, +LevelCap, -RemainingHp, -BestPokemon)
% It finds the best This predicate creates a list of all Pokemons and calls best_pokemon with more arguments which is actually another predicate that helps to make 
% fight recursively with the help of decide_best until the PokemonList finishes. It runs until it fights with all Pokemons with the help of length of the whole 
% pokemon list and index counter. When it finishes, BestPokemon and RemainingHp get the current greatest values.
 
  
decide_best(EnemyPokemon, Pokemon2, LevelCap, Pokemon2Hp, _, CurHp, A, B, Cnew, Inew, List, BestPokemon, RemainingHp, N):-notinst(CurHp), A=Pokemon2, B=Pokemon2Hp, best_pokemon(EnemyPokemon, LevelCap, RemainingHp, BestPokemon, A, B, _, _, Cnew, Inew, List, N).
decide_best(EnemyPokemon, Pokemon2, LevelCap, Pokemon2Hp, _, CurHp, A, B, Cnew, Inew, List, BestPokemon, RemainingHp, N):-inst(CurHp), Pokemon2Hp>=CurHp, A=Pokemon2, B=Pokemon2Hp, best_pokemon(EnemyPokemon, LevelCap, RemainingHp, BestPokemon, A, B, _, _, Cnew, Inew, List, N).
decide_best(EnemyPokemon, _, LevelCap, Pokemon2Hp, CurBest, CurHp, _, _, Cnew, Inew, List, BestPokemon, RemainingHp, N):-inst(CurHp), Pokemon2Hp<CurHp, best_pokemon(EnemyPokemon, LevelCap, RemainingHp, BestPokemon, CurBest, CurHp, _, _, Cnew, Inew, List, N).         

best_pokemon(EnemyPokemon, LevelCap, RemainingHp, BestPokemon, CurBest, CurHp, A, B, Count, I, List, N):-nth0(I, List, Pokemon2),
																										 Cnew is Count+1, Cnew<N,
																										 Inew is I+1,
																										 pokemon_fight(EnemyPokemon, LevelCap, Pokemon2, LevelCap, _, Pokemon2Hp, _),
                                                                                                         decide_best(EnemyPokemon, Pokemon2, LevelCap, Pokemon2Hp, CurBest, CurHp, A, B, Cnew, Inew, List, BestPokemon, RemainingHp, N), !. 
																										 
best_pokemon(_, _, RemainingHp, BestPokemon, CurBest, CurHp, _, _, Count, _, _, N):-Cnew is Count+1, 
                                                                                    Cnew>=N, 
																					BestPokemon=CurBest, 
																					RemainingHp is CurHp.

best_pokemon(EnemyPokemon, LevelCap, RemainingHp, BestPokemon):-findall(X, pokemon_stats(X, _, _, _, _), List), 
																length(List, N),
																Nnew is N+1,
																best_pokemon(EnemyPokemon, LevelCap, RemainingHp, BestPokemon, _, _, _, _, 0, 0, List, Nnew).


																
% ---a helper predicate--- best_pokemon_team_recursive(+PokemonTeam1, +PokemonLevels, -PokemonTeam, ?OldTeam)
% This predicate is used in best_pokemon_ team. It finds the best pokemons until the team finishes. 
% These best pokemons are added to the list with the help of append. When the team finishes, it makes the PokemonTeam equal to the CurrentTeam. 
% The complete version of this predicate is written in best_pokemon_team predicate.


% best_pokemon_team(+OpponentTrainer, -PokemonTeam)
% This predicate finds the best Pokemon team against the given enemy team. With the help of pokemon_trainer, it gets the Pokemon team and calls the 
% best_pokemon_team_recursive to find all best Pokemons and create the best Pokemon team.

best_pokemon_team_recursive([], [], CurrentTeam, CurrentTeam).
best_pokemon_team_recursive([HeadT|TailT], [HeadL|TailL], PokemonTeam, OldTeam):-best_pokemon(HeadT, HeadL, _, BestPokemon), 
                                                                       append(OldTeam, [BestPokemon], CurrentTeam), 
																       best_pokemon_team_recursive(TailT, TailL, PokemonTeam, CurrentTeam).
best_pokemon_team(OpponentTrainer, PokemonTeam):-pokemon_trainer(OpponentTrainer, PokemonTeam1, PokemonLevels),
                                                 best_pokemon_team_recursive(PokemonTeam1, PokemonLevels, PokemonTeam, _), !. 
												 
												 

% ---a helper predicate--- pokemon_types_recursive(+TypeList, +InitialPokemonList, -PokemonList, ?CurrentList)
% This predicate is used in pokemon_types predicate. It runs recursively until InitialPokemonList finishes. With the help of pokemon_stats and member predicates,
% it gets types of Pokemon and decides whether it contains at least one of the types in TypeList. Firstly, it looks for the head type of the Pokemon. If the TypeList
% doesn't contain head type, it should continue with other types of Pokemon, so pokemon_tail_types is called. However, if it contains the head type, it adds the Pokemon to the
% currentList and continues with the new Pokemons from InitialPokemonList. When the InitialPokemonList finishes, it makes the PokemonList equal to the
% CurrentList. The complete version of this predicate is written in pokemon_types predicate. 



% ---a helper predicate--- pokemon_tailtypes(+TypeList, +InitialPokemonList, -PokemonList, ?CurrentList)
% This predicate is used in pokemon_types predicate. This predicate recursively looks for a Pokemon type that is also in TypeList until the Pokemon's typeList
% finishes. If it finds, it add the Pokemon to the current Pokemon list and gets new Pokemons from the InitialPokemonList. If it doesn't find, it doesn't add
% the current Pokemon and continue with new Pokemons by calling pokemon_types_recursive. The complete version of this predicate is written in pokemon_types predicate. 


% pokemon_types(+TypeList, ?InitialPokemonList, -PokemonList)
% This predicate finds the Pokemons in the InitialPokemonList that are at least one of the types from the TypeList. It uses pokemon_tailtypes and
% and pokemon_types_recursive predicates which recursively search for the types and add Pokemons to the list according to that rule. It
% calls pokemon_types_recursive to make the recursion start with a currently empty list.

pokemon_tailtypes(TypeList, [_|TailPokemon], PokemonList, List, []):-pokemon_types_recursive(TypeList, TailPokemon, PokemonList, List).
pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], PokemonList, OldList, [HeadTailTList|_]):- member(HeadTailTList, TypeList), 
                                                                                                  append(OldList, [HeadPokemon], NewList), 
																							      pokemon_types_recursive(TypeList, TailPokemon, PokemonList, NewList), !.																			  
pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], PokemonList, List, [HeadTailTList|TailTailTlist]):- not(member(HeadTailTList, TypeList)), 
                                                                                                           pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], PokemonList, List, TailTailTlist), !.																							   

pokemon_types_recursive(_, [], CurrentList, CurrentList).
pokemon_types_recursive(TypeList, [HeadPokemon|TailPokemon], PokemonList, CurrentList):- pokemon_stats(HeadPokemon, [HeadPTList|_], _, _, _), 
                                                                                         member(HeadPTList, TypeList), 
																		                 append(CurrentList, [HeadPokemon], NewList), 
																		                 pokemon_types_recursive(TypeList, TailPokemon, PokemonList, NewList), !.
pokemon_types_recursive(TypeList, [HeadPokemon|TailPokemon], PokemonList, List):- pokemon_stats(HeadPokemon, [HeadPTList|TailTList], _, _, _), 
                                                                                  not(member(HeadPTList, TypeList)), 
																		          pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], PokemonList, List, TailTList), !.

pokemon_types(TypeList, InitialPokemonList, PokemonList):- pokemon_types_recursive(TypeList, InitialPokemonList, PokemonList, _), !.



% ---helper predicates--- compareH, compareA, compareD(R, +List1, +List2)
% These predicates are needed in predsort predicate in order to sort the lists by looking their spesific indexes.

compareH(R,[_,H1,_, _],[_,H2,_, _]) :- H1=\=H2, !, compare(R,H2,H1).
compareH(R,E1,E2) :- compare(R,E1,E2).
compareA(R,[_,_,A1, _],[_,_,A2, _]) :- A1=\=A2, !, compare(R,A2,A1).
compareA(R,E1,E2) :- compare(R,E1,E2).
compareD(R,[_,_,_, D1],[_,_,_, D2]) :- D1=\=D2, !, compare(R,D2,D1).
compareD(R,E1,E2) :- compare(R,E1,E2). 



% ---helper predicates--- make_health_list, make_attack_list, make_defense_list(+Count, -PokemonTeam, +Length)
% These predicates are used in generate_pokemon_team predicate. If the length of the list is greater than or equal to 2, it predsorts,
% takes first N(count) elements and make the PokemonTeam equal to this SortedList. If length is less than 2, it doesn't need to sort. 
% It just makes the PokemonTeam equal to the currentlist(FullList). The complete version of this predicates are written in generate_pokemon_team predicate. 



% ---helper predicate---take(+List, +Count, -NewList)
% It takes first N(count) elements and create a NewList with these elements. It is used in generate_pokemon_team predicate.
 
take(List, Count, NewList) :- findall(Element, (nth1(Index,List,Element), Index=<Count), NewList).

% ---helper predicate--- make_list(+Criterion, +Count, -PokemonTeam, +PokemonList, -FullList, +OldList)
% It is used in generate_pokemon_team predicate. It takes a Pokemonlist and creates a list of lists which contains these Pokemons health, attack and defense
% points. After the PokemonList finishes, it calls make_health_list, make_attack_list or make_defense_list in order to sort this list according to te given
% criterion. The complete version of this predicates are written in generate_pokemon_team predicate.



% ---a helper predicate--- generate_team(+TypeList, +InitialPokemonList, +Criterion, +Count, -PokemonTeam, -PokemonList, +CurrentList)
% It is used in generate_pokemon_team predicate. With the help of pokemon_tailtypes predicate, it recursively checks whether Pokemons in InitialPokemonList
% contains a type that is in TypeList. If it doesn't contain, it adds the current Pokemon to the current list. If it contains, it doesn't add the Pokemon and
% continues to run with new Pokemons. When the initial Pokemon list finishes, it makes PokemonList equal to the curren PokemonList and calls make_list
% predicate to continue with the job.

% ---a helper predicate--- pokemon_tailtypes(+TypeList, +InitialPokemonList, +Criterion, +Count, -PokemonTeam, -PokemonList, List, +PokemonTypeList)
% This predicate is used in generate_pokemon_team. It works with the generate_team to search recursively a type in a pokemon type list that is in type list.
% If it doesn't find, it adds the current Pokemon. If it finds, it doesn't add it and continue with the new Pokemons.

% generate_pokemon_team(+LikedTypes, +DislikedTypes, +Criterion, +Count, -PokemonTeam)
% It generates a Pokemon team based on liked and disliked types according to the given criteria. With the help of generate_team and pokemon_tailtypes,
% it recursively creates a big list with Pokemons which contain at least one type of LikedTypes and don't contain any of the types in DislikedTypes. After that,
% it goes to make_list and creates a list of list which contains these Pokemons and their health, attack, defense points. Lastly, according to the criterion
% it goes make_health_list, make_attack_list or make_defense_list ir order to get the last form of the list.

make_health_list(Count, PokemonTeam, FullList, Length):-(Length>=2, predsort(compareH, FullList, SortedList), take(SortedList, Count, PokemonTeam)); PokemonTeam=FullList.
make_attack_list(Count, PokemonTeam, FullList, Length):-(Length>=2, predsort(compareA, FullList, SortedList), take(SortedList, Count, PokemonTeam)); PokemonTeam=FullList.
make_defense_list(Count, PokemonTeam, FullList, Length):-(Length>=2, predsort(compareD, FullList, SortedList), take(SortedList, Count, PokemonTeam)); PokemonTeam=FullList.

make_list(Criterion, Count, PokemonTeam, [], FullList, X):-Criterion=h, FullList=X, length(FullList, Length), make_health_list(Count, PokemonTeam, FullList, Length).
make_list(Criterion, Count, PokemonTeam, [], FullList, X):-Criterion=a, FullList=X, length(FullList, Length), make_attack_list(Count, PokemonTeam, FullList, Length).
make_list(Criterion, Count, PokemonTeam, [], FullList, X):-Criterion=d, FullList=X, length(FullList, Length), make_defense_list(Count, PokemonTeam, FullList, Length).
make_list(Criterion, Count, PokemonTeam, [HeadPokemonList|TailPokemonList], FullList, OldList):-pokemon_stats(HeadPokemonList, _, Health, Attack, Defense),
                                                                                                append(OldList, [[HeadPokemonList, Health, Attack, Defense]], NewList), 
																						        make_list(Criterion, Count, PokemonTeam, TailPokemonList, FullList, NewList). 
 
pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List, []):- append(List, [HeadPokemon], NewList), 
                                                                                                               generate_team(TypeList, TailPokemon, Criterion, Count, PokemonTeam, PokemonList, NewList), !.
pokemon_tailtypes(TypeList, [_|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List, [HeadTailTList|_]):-member(HeadTailTList, TypeList), 
                                                                                                                   generate_team(TypeList, TailPokemon, Criterion, Count, PokemonTeam, PokemonList, List).
pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List, [HeadTailTList|TailTailTlist]):-not(member(HeadTailTList, TypeList)), 
                                                                                                                                         pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List, TailTailTlist), !.
 
generate_team(_, [], Criterion, Count, PokemonTeam, PokemonList, List):-PokemonList=List, make_list(Criterion, Count, PokemonTeam, PokemonList, _, _).
generate_team(TypeList, [HeadPokemon|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List):- pokemon_stats(HeadPokemon, [HeadPTList|_], _, _, _), 
                                                                                                       member(HeadPTList, TypeList), 
																									   generate_team(TypeList, TailPokemon, Criterion, Count, PokemonTeam, PokemonList, List), !.
generate_team(TypeList, [HeadPokemon|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List):- pokemon_stats(HeadPokemon, [HeadPTList|TailTList], _, _, _), 
                                                                                                       not(member(HeadPTList, TypeList)), 
																									   pokemon_tailtypes(TypeList, [HeadPokemon|TailPokemon], Criterion, Count, PokemonTeam, PokemonList, List, TailTList), !.


generate_pokemon_team(LikedTypes, DislikedTypes, Criterion, Count, PokemonTeam):-findall(X, pokemon_stats(X, _, _, _, _), List),
																				 pokemon_types(LikedTypes, List, NewList),
																				 generate_team(DislikedTypes, NewList, Criterion, Count, PokemonTeam, _, _).
	

