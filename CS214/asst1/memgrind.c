#include <stdlib.h>
#include "mymalloc.h"
#include <time.h>


struct timespec diff(struct timespec start, struct timespec end)
{
    struct timespec temp;
    if ((end.tv_nsec-start.tv_nsec)<0) {
        temp.tv_sec = end.tv_sec-start.tv_sec-1;
        temp.tv_nsec = 1000000000+end.tv_nsec-start.tv_nsec;
    } else {
        temp.tv_sec = end.tv_sec-start.tv_sec;
        temp.tv_nsec = end.tv_nsec-start.tv_nsec;
    }
    return temp;
}

int main()
{
	
	long int aTotal = 0;
	long int bTotal = 0;
	long int cTotal = 0;
	long int dTotal = 0;
	long int eTotal = 0;
	long int fTotal = 0;

	int i, r, c, count, numMallocs, index, spaceUsed, bytesNeeded;
	char* ptr;
	char* arrayB[50];
	char* arrayC[50];
	char* arrayD[50];
	char* arrayF[5][10];

	struct timespec start;
	struct timespec end;

	int counter;
	for(counter = 0; counter < 100; counter ++)
	{
		//----------------Test A----------------------------------------------------------
	
		clock_gettime(CLOCK_MONOTONIC, &start);
	
		for(i = 0; i < 150; i++)
	        {
	                ptr = (char*)malloc(1);
	                free(ptr);
	        }
	
		clock_gettime(CLOCK_MONOTONIC, &end);
	
	        aTotal += diff(start, end).tv_nsec;
		
//		printf("finish test a\n");	
		
		//----------------Test B----------------------------------------------------------		

		clock_gettime(CLOCK_MONOTONIC, &start);
	
		for(count = 0; count < 3; count ++)
		{
			for(i = 0; i < 50; i ++)
			{
				arrayB[i] = (char*)malloc(1);
			}
			for(i = 0; i < 50; i ++)
	                {
	                        free(arrayB[i]);
	                }
		}
	
		clock_gettime(CLOCK_MONOTONIC, &end);
	
	        bTotal += diff(start, end).tv_nsec;

//		printf("finish test b\n");	

	
		//----------------Test C----------------------------------------------------------	


		clock_gettime(CLOCK_MONOTONIC, &start);

		numMallocs = 0;
		index = 0;	

		while(numMallocs < 50)
		{
			if(index > 0 && (rand() % 2))
			{
				free(arrayC[index]);
				index --;
			}
			else
			{
				index ++;
				arrayC[index] = (char*)malloc(1);
				numMallocs ++;
			}
		}

		while(index >= 0)
		{
			free(arrayC[index]);
			index --;
		}


		clock_gettime(CLOCK_MONOTONIC, &end);


	        cTotal += diff(start, end).tv_nsec;

//		printf("finish test c\n");	


		//----------------Test D----------------------------------------------------------	

		clock_gettime(CLOCK_MONOTONIC, &end);

	        numMallocs = 0;
		spaceUsed = 0;
	        index = 0;

	        while(numMallocs < 50)
	        {
        	        if(index > 0 && (rand() % 2))
       		        {
				spaceUsed -= sizeof(arrayD[index]);
	                        free(arrayD[index]);
	                        index --;
	                }
	                else
	                {
				bytesNeeded = (rand() % 64) + 1;
				if(spaceUsed + bytesNeeded <= 4096)
				{
					index ++;
	                        	arrayD[index] = (char*)malloc(bytesNeeded);
					spaceUsed += bytesNeeded;
					numMallocs ++;
				}
	                }
	        }
	
	        while(index >= 0)
	        {
	                free(arrayD[index]);
	                index --;
	        }
	


		clock_gettime(CLOCK_MONOTONIC, &end);

	 	dTotal += diff(start, end).tv_nsec;

//		printf("finish test d\n");	


		//----------------Test E----------------------------------------------------------	

		clock_gettime(CLOCK_MONOTONIC, &start);
		
		for(i = 0; i < 150; i ++)
		{
			ptr = (char*)malloc(4096);
			free(ptr);
		}

		clock_gettime(CLOCK_MONOTONIC, &end);

                eTotal += diff(start, end).tv_nsec;

//              printf("finish test e\n");

		//----------------Test F----------------------------------------------------------	

		clock_gettime(CLOCK_MONOTONIC, &start);

		for(r = 0; r < 5; r ++)
		{
			for(c = 0; c < 10; c++)
			{
				arrayF[r][c] = (char*)malloc(1);
			}
		}

		for(r = 0; r < 5; r ++)
                {
                        for(c = 0; c < 10; c++)
                        {
                                free(arrayF[r][c]);
                        }
                }

                clock_gettime(CLOCK_MONOTONIC, &end);

                fTotal += diff(start, end).tv_nsec;

//              printf("finish test f\n");




	}//end of 100 iterations


	printf("average test A time: %ld ns\n", (aTotal/100));
	printf("average test B time: %ld ns\n", (bTotal/100));
	printf("average test C time: %ld ns\n", (cTotal/100));
	printf("average test D time: %ld ns\n", (dTotal/100));
	printf("average test E time: %ld ns\n", (eTotal/100));
	printf("average test F time: %ld ns\n", (fTotal/100));
	
		
	


}

	




