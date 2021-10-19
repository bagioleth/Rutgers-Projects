//	Group Members: Ramit Sharma and Brian Angioletti

#include "mymalloc.h"

// Takes in the amount of memory a user wants to allocate and checks to see if there is enough room in the heap

void* mymalloc(size_t size, char* string, size_t line){
		
	if(HEAP_SIZE - memory_count < size)
	{
		return NULL;
	}

	char* open_position = seek_heap(&heap[0], size);
	
	//The seek_heap returns NULL if no block can fit the data being requested
	if(open_position == NULL)
	{
		return NULL;
	}
	
	open_position = partition_mem_node(open_position, size);
	
	//return a void* ptr to the new memory location
	return (void*)(open_position);
}

void myfree(void* mem_addr, char* string, size_t line)
{
	
	char* itr = &heap[0];
	
	int x = 0;
	for(x = 0; x < mem_node_count; x++)
	{	
		if(mem_addr == (itr + sizeof(MemNode*) + 1) )
		{
			((MemNode*)(itr))->dirty_bit = false;
			purge_heap(itr,itr,0);	
			break;
		}		
		//update itr to the next position in the heap
		size_t displacement = ((MemNode*)(itr))->mem_size + sizeof(MemNode*) + 1;
		itr = itr + displacement;
	}
}

// Defragment memory when ever we try to free a node
void purge_heap(char* dest, char* mem_addr, int nodes)
{
	size_t displacement = ((MemNode*)(dest))->mem_size + sizeof(MemNode*) + 1;
	
	if(mem_node_count == 1)
	{
		return;
	} 

	if( ((MemNode*)(dest))->dirty_bit == false && ((MemNode*)(dest+displacement))->dirty_bit == false 		&& (int)((MemNode*)(dest+displacement))->mem_size != 0)
	{				
		//merge the size of the two nodes in the left one	
		((MemNode*)(dest))->mem_size += ((MemNode*)(dest+displacement))->mem_size+sizeof(MemNode*);
		
		mem_node_count-=1;
		purge_heap(dest, dest+displacement, nodes + 1);
	}
}

char* seek_heap(char* starting_block, size_t request_size){	
		
	//ptr that will be used for traversing through the heap
	char* itr = starting_block;
	
	int x = 0;
	for(x = 0; x < mem_node_count; x++){
		
		//if there is no data written to said position 
		//and theres enough room at the current location
		if( ((MemNode*)(itr))->dirty_bit == false && ((MemNode*)(itr))-> mem_size >=request_size)
		{
			return itr;
		}
		size_t displacement = ((MemNode*)(itr))->mem_size + sizeof(MemNode*) +1;
		itr = itr + displacement;
	}
	return NULL;
}

char* partition_mem_node(char* open_position, size_t size){
	
	//store the size of the original block of memory
	size_t old_size = ((MemNode*)(open_position))->mem_size;
	((MemNode*)(open_position))->mem_size = size;
	((MemNode*)(open_position))->dirty_bit = true;

	char* write_pos = open_position + sizeof(MemNode*) + 1;

	char* new_mem_node = write_pos + request_size;
	((MemNode*)(new_mem_node))->mem_size = old_size - size - sizeof(MemNode*);
	((MemNode*)(new_mem_node))->dirty_bit = false;
	
	memory_count += size + sizeof(MemNode*);
	mem_node_count = mem_node_count + 1;

	return write_pos;
}

void init_heap()
{
	int x = 0;
	for(x = 0; x < HEAP_SIZE; x++)
	{
		heap[x]='0';
	}	
	MemNode* data = (MemNode*)(&heap[0]);
	data->dirty_bit = false;
	data->mem_size = HEAP_SIZE - sizeof(data);

	//update the global heap counter & MemNode counter
	memory_count += sizeof(data);
	mem_node_count = 1;
}

void print_heap_status()
{
	int x = 0;
	char* itr = &heap[0];
	for(x = 0; x < mem_node_count; x++)
	{
		//printf("Data Entry:\t%d\n", x);
		//printf("Mem Address:\t%p\n", itr);
		//printf("Dirty Bit:\t%d\n", ((MemNode*)(itr))->dirty_bit); 
		//printf("Entry Size:\t%d\n\n", ((MemNode*)(itr))->mem_size);
		
		size_t displacement = ((MemNode*)(itr))->mem_size + sizeof(MemNode*) +1;

		itr = itr + displacement;	
	} 
}
