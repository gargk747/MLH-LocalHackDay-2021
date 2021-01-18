import operator
import rake
import io
import excel
from difflib import SequenceMatcher

#list of stop words
stoppath = "SmartStoplist.txt";

#Rake - Rapid Automatic Keyword Extraction
rake_obj = rake.Rake(stoppath);

#algorithm works per sentence
def findJargonPhrases(keywords,jargonDict,originalSentence):
    if isinstance(keywords,str):
        sentenceKeywords = [keywords.lower()]
    elif isinstance(keywords,list):
        sentenceKeywords = keywords
    jargOptions = {}
    possibleMatches = []
    for medTerm in jargonDict.keys():
        check = sum([1 for item in sentenceKeywords if item in medTerm.lower()])
        if check >= len(sentenceKeywords) - 1 and check > 0:
            possibleMatches.append(medTerm)
    jargOptions[originalSentence] = possibleMatches
    #returns a dictionary with the sentence being the key and the value being an array of possible matches
    #might need another function to figure out which of the values is the best
    return jargOptions
    
#check if the medTerm is a single word
def findJargonKeywords(keywords,jargonDict,originalSentence):
    # print keywords
    jargOptions = {}
    sentenceKeywords = keywords
    possibleMatches = []
    for medTerm in jargonDict.keys():
        if (medTerm.lower() in sentenceKeywords):
            possibleMatches.append(medTerm)
    jargOptions[originalSentence] = possibleMatches
    return jargOptions
    
def testDriver(sentence,jargonDict):
    #case 1 - sentence match
    jargon = findJargonPhrases(sentence,jargonDict,sentence)
    if len(jargon.values()[0])==0: #case 2 - phrase match
        phraseList = rake_obj.run(sentence)
        phraseList = [i[0] for i in phraseList]
        jargon = findJargonPhrases(phraseList,jargonDict,sentence)
    if len(jargon.values()[0])==0: #case 3 - keyword match
        keywordArray = []
        for phrase in phraseList:
            keywordArray.extend(phrase.split(" "))
        jargon = findJargonPhrases(keywordArray,jargonDict,sentence)
    if len(jargon.values()[0])==0:#case 4 - find single words
        jargon = findJargonKeywords(keywordArray,jargonDict,sentence);
    jargon = determineBestMatch2(jargon)
    return jargon


def determineBestMatch(jargon):
    if len(jargon.values()[0]) == 1:
        return jargon.values()[0][0]
    originalSentence = jargon.keys()[0]
    matchScores = []
    score = 0
    for option in jargon.values()[0]:
        for i in range(min(len(option),len(originalSentence))):
            score = score + -1*(option[i]!=originalSentence[i]) + 5*(option[i]==originalSentence[i])
        matchScores.append((score,option))
        score = 0
    print sorted(matchScores[-1][1])
    return sorted(matchScores)[-1][1]

def determineBestMatch2(jargonDict):
    if len(jargonDict.values()[0]) == 1:
        return jargonDict.values()[0][0]
    compareKeywords = {}
    for option in jargonDict.values()[0]:
        medKeywords = rake_obj.run(option)
        medKeywords = [m[0] for m in medKeywords]
        compareKeywords[option] = medKeywords
    originalSentence = jargonDict.keys()[0]
    matchScores = []
    for option in jargonDict.values()[0]:
        matchScores.append((SequenceMatcher(None, option, originalSentence).ratio(),option))
    if len(matchScores) != 0 and matchScores[-1][0] >= 0.1:
        return sorted(matchScores)[-1][1]
    else:
        return ''

def parseFromPatientRecord(text):
    text.strip()
    return [x for x in text.split('.') if x]
    
if __name__ == '__main__':
    fil = "MammoTerms.xls"
    jargonDict = excel.parseFromExcel(fil)
    text = "There are scattered fibroglandular elements in both breasts. There is possible architectural distortion in the left breast middle depth central to the nipple seen on the mediolateral oblique view only. No other suspicious masses or calcifications are seen in either breast."
    text2 = "The possible architectural distortion in the left breast middle depth central to the nipple seen on the mediolateral oblique view only appears indeterminate.  Additional views are recommended."
    text3 = "BI-RADS: 0 Incomplete Assessment"
    text4 = "The tissue of both breasts is predominantly fatty. No suspicious masses, calcifications, or other findings are seen in either breast."
    text5 = "There is no mammographic evidence of malignancy.  A 1 year screening mammogram is recommended."
    text6 = "There are scattered areas of fibroglandular density"
    text7 = "breasts are fatty"
    textB = "Comparison is made to exams dated:  7/2/2012 mammogram - Cancer Center-Breast Imaging and 6/29/2011 mammogram - South Breast Imaging. The tissue of both breasts is predominantly fatty. No suspicious masses, calcifications, or other findings are seen in either breast. There has been no significant interval change. IMPRESSION: NEGATIVE There is no mammographic evidence of malignancy.  A 1 year screening mammogram is recommended. The patient was notified of the results. BI-RADS: 1 Negative"
    text = parseFromPatientRecord(textB.strip())
    #functions work per sentence
    translatedReports = {}
    for i in range(len(text)):
        string = testDriver(text[i],jargonDict)
        print("\nINPUT STRING: " + text[i])
        print ("MATCHED STRING: " + string)
        print ("TRANSLATION: " + jargonDict[string])
        if (jargonDict[string]).strip():
            translatedReports[text[i]] = jargonDict[string]
    
def getTransalation(input):
    fil = "MammoTerms.xls"
    jargonDict = excel.parseFromExcel(fil)
    text = parseFromPatientRecord(input.strip())
    #functions work per sentence
    translatedReports = {}
    for i in range(len(text)):
        string = testDriver(text[i],jargonDict)
        translatedReports[text[i]] = jargonDict[string]
        if (jargonDict[string]).strip():
            translatedReports[text[i]] = jargonDict[string]
    return translatedReports
    