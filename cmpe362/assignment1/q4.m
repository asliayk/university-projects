std = [1 sqrt(8) 8 16];
stdc = [1; sqrt(8); 8; 16;];
a = std(2:3);
b = stdc(4);
% standard deviation vector, standard deviation is the square root of the variance
H=normrnd(0,repmat(std,5000,1)); % sample size is 5000
hist(H,500), xlim([-20.0 20.0]), legend(["r1"; "r2"; "r3"; "r4"]);