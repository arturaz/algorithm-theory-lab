#include <stdio.h>	// printf();
#include <unistd.h>	// getopt;
#include <stdlib.h>
#include <stdbool.h>

void randomize(int size, bool real);
void printHelp();
int main(int argc, char **argv)
{
	int size = 0;
	bool real = false;

	char c;
	while ((c = getopt (argc, argv, "n:Rh")) != -1)
		switch (c)
		{
			case 'n':
				size = atoi(optarg);
				break;
			case 'R':
				real = true;
				break;
			case '?':
				if (optopt == 'n')
					fprintf (stderr, "Option -%c requires an argument.\n", optopt);
				else if (isprint (optopt))
					fprintf (stderr, "Unknown option `-%c'.\n", optopt);
				else
					fprintf (stderr, "Unknown option character `\\x%x'.\n", optopt);
				return 1;
			case 'h':
				printHelp();
			default:
				exit(1);
		}
		
	randomize(size, real);
}

void randomize(int size, bool real)
{
  printf("%d\n\n", size);
  int max_num = 50000;
	int i = 0;
	int r_number;
	srandom(time(0));
	if(!real)
	{
		for(i; i < size; i++)
		{
			r_number=rand();
			printf("%d \n",r_number % max_num);
		}
	}
	else
	{
		int temp;
		for(i; i < size; i++)
		{
			r_number = rand();
			temp = rand();
			if(!(temp%2))
				r_number*=-1;
			printf("%d \n",r_number % max_num);
		}
	}
}

void printHelp()
{
	fprintf(stderr,"random: usage\n");
	fprintf(stderr,"\t n number, specify number to generate.\n");
	fprintf(stderr,"\t R, randomise in REAL set.\n");
}
