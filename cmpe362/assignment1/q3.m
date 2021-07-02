z = rand(1, 401);
% generates uniformly distributed number between 0 and 1
z = 0.1.*z;
% numbers are multiplied by 0.1
t = (-2:0.01:2);
% t vector is given
y20 = z;
% y20, y21,….,y29 are given
y21 = z+t;
y22 = z+y1;
y23 = z.*y1;
y24 = sin(2*pi.*z).*t;
y25 = sin(2*pi.*(t+z));
y26 = z.*y2;
y27 = sin(2*pi.*(t+10.*z));
y28 = y1./z;
y29 = y21+y22+y23+y24+y25+y26+y27+y28;

subplot(5,2,1), plot(y20), xlim([0 401]), title('y20');
subplot(5,2,2), plot(t, y21), xlim([-2 2]), title('y21');
subplot(5,2,3), plot(t, y22), xlim([-2 2]), title('y22');
subplot(5,2,4), plot(t, y23), xlim([-2 2]), title('y23');
subplot(5,2,5), plot(t, y24), xlim([-2 2]), title('y24');
subplot(5,2,6), plot(t, y25), xlim([-2 2]), title('y25');
subplot(5,2,7), plot(t, y26), xlim([-2 2]), title('y26');
subplot(5,2,8), plot(t, y27), xlim([-2 2]), title('y27');
subplot(5,2,9), plot(t, y28), xlim([-2 2]), title('y28');
subplot(5,2,10), plot(t, y29), xlim([-2 2]), title('y29');