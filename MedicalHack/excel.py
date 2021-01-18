#importing data from excel file

import xlrd
import csv
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

#excel to dictionary parser 
def parseFromExcel(fileName):
	workbook = xlrd.open_workbook(fileName)
	#assume data is always present in sheet 1
	worksheet = workbook.sheet_by_index(0)
    
	total_rows = worksheet.nrows
	total_cols = worksheet.ncols
    
	dict = {}
    #iterate through each row and generate the dictionary
	for x in range(total_rows):
	    key = str(worksheet.cell(x,0).value)
	    val =  str(worksheet.cell(x,1).value)
	    dict[key] = val
	    
	#print(dict)
	return dict

    