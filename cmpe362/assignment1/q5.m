std = [1 2 1 2]; % standard deviation vector, standard deviation is the square root of the variance
me = [10 20 -10 -20]; % mean vector
H = normrnd(repmat(me, 5000,1), repmat(std, 5000,1)); % sample size is 5000
hist(H,300), xlim([-40.0 40.0]), legend(["r5"; "r6"; "r7"; "r8"]); 