r11 = 8.*rand(5000,1)-4; % 5000 random numbers between -4 and 4
r21 = 40.*rand(5000,1)-20; % 5000 random numbers between -20 and 20
H = [r11 r21]; % putting two vectors in a matrix to show it on same figure
hist(H, 300), xlim([-25.0 25.0]); 