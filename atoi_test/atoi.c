#include "stdio.h"
#include "stdlib.h"
#include "limits.h"
#include "string.h"
/*
 * atoi.c
 *
 *  Created on: May 28, 2013
 *      Author: grahampoor
 */
int gvp_atoi(const char* int_as_string, int len) {
	// Accepts only 0-9 and the '-' in the place where the most significant digit would be for a positive number.
	// Depends on length doesn't use null terminator.
	// Disclaimer just a quick and dirty coding exercise
	// No error checking for non digits ~[0-9] or null string. No overflow underflow etc,
	// No handling of  number formating chars like ',' '+', etc.

	int multiplyer = 1;
	int result = 0; //
	// Test for first char a negative sign, '-' (not supporting first char as a  '+')
	short signedNumIndex = (int_as_string[0] == '-') ? 1 : 0;

	int i = len-1;

	for (; i >=signedNumIndex; i--) {
		int digit_from_char = int_as_string[i] - '0';
		//printf("%s[%d]=%c  digit_from_char = %d \n",int_as_string,i,int_as_string[i],digit_from_char);
		result += digit_from_char * multiplyer;
		multiplyer *= 10;
	}
	if (signedNumIndex)
		result *= (-1);
	return result;
}
// Rough test of coding sample get's some false negatives like for "010" would not detect some errors because it is using %d to check for a matching result.
// Next step would be to state why the expected errors are expected and decide how to catch or that those cases are outside the functions domain. And whether the range
// of returned values is reasonable in error and other cases (currently it is probably not because it could catch overflow errors and return an agreed error value INT_MIN or something.
int main(int arg_num, char* args[]) {
	// Test data, tests after " expected errors" test are fail as expected.
	const char* testNumStrings[] = { "1","2","12","21","123","12345","0","10203","123456789","-1","-123","-1742872343","987654321","65537","9999999","-1","expected errors","999999999999","-99999999999999","abc3","+323","  4","-","expected false errors follow","0001","00100",0 };
	char test_results_buffer[32];
	int count = 0;
	int test_result = 0;
	while (testNumStrings[count]) {
		test_result=gvp_atoi(testNumStrings[count],strlen(testNumStrings[count]));

		sprintf(test_results_buffer,"%d",test_result);

		if (0==strcmp(test_results_buffer,testNumStrings[count]))
			printf("%d) Correct=> (gvp_atoi()==%d)==%s",count, test_result,testNumStrings[count]);
		else
			printf("%d) ERROR! (gvp_atoi()==%d) NOT equal %s",count,test_result,testNumStrings[count]);
		puts("\n");
		count++;
	}
	return EXIT_SUCCESS;

}
