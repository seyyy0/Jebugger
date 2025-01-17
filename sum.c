#include <stdio.h>

int main() {

    int x = 0;

    int a,b,c,d;
    int sum;

    a = 0;
    b = 1;
    c = 2;
    d = 3;
    sum = a+b+c+d;

    printf("Sum of a, b, c and d is: %d\n", sum);

    a = a+1;
    b = b+1;
    c = c+1;
    d = d+1;
    sum = a+b+c+d;

    printf("New sum of a, b, c and d is: %d\n", sum);

    getchar();
    getchar();

    return 0;
}