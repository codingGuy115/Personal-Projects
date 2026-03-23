# 2/7/2026 
# This project is meant to help organize myself
import math
import os

# 0-Sat  1-Sun  2-Mon  3-Tues  4-Wed  5-Thur  6-Fri --> (note this is arbitrary)

class Assignment:
    def __init__(self, subject:str, name:str, due_date:int, num_questions:int):
        self.subject = subject
        self.name = name
        self.due_date = due_date
        self.num_questions = num_questions

class Task:
    def __init__(self, subjAndName:str, num_Qs:int):
        self.subjAndName = subjAndName
        self.num_Qs = num_Qs
        self.complete = False

class Day:
    def __init__(self, name:str):
        self.name = name
        self.tasks:list[Task] = []
    
    def addTask(self, task:Task):
        self.tasks.append(task)
    
    def removeTask(self, subjAndName:str):
        for i in range(0, len(self.tasks)):
            if self.tasks[i].subjAndName == subjAndName:
                self.tasks.remove(self.tasks[i])
                i-=1

    def __str__(self): 
        lines = ['****** '+self.name+' *******']
        for t in self.tasks:
            lines.append(t.subjAndName +": "+str(t.num_Qs))
        return "\n".join(lines)

    __repr__=__str__


#---------------------------------------------------------------------------
#           Classes ^^
# ----------Functions vv ---------------------------------------------------

'''finds number of days free from today to due date inclusive'''
def DaysFreeToWork(today:int, due_date:int, days:dict):
    if due_date < today: #ensure due date isnt before today
        return -1
    
    freeDays = []
    count = 0
    for i in range(today, due_date+1):
        if days[i]=='y':
            count += 1
            freeDays.append(i)
    return (count, freeDays)


# front loads the assignment questions as much as possible
def allocateTasks(count_freeDays, assignment:Assignment, day_plans:list[Day])->None:
    if count_freeDays == -1 or count_freeDays[0] <= 0:
        return

    num_days = count_freeDays[0]
    free_days = count_freeDays[1]
    chunk_size = math.ceil(assignment.num_questions / num_days)
    remaining_questions = assignment.num_questions

    for i in range(0, len(free_days)):
        #front-load rounded-up chunks, then place remaining questions on the last day.
        if i == len(free_days) - 1:
            questions_today = remaining_questions
        else:
            questions_today = min(chunk_size, remaining_questions)

        if questions_today > 0:
            day_plans[free_days[i]].addTask(Task(assignment.subject + " " + assignment.name, questions_today))
            remaining_questions -= questions_today


def writeToMemoryALL(assignments:list[Assignment]):
    with open('memory.txt', 'w') as f:
        for a in assignments:
            f.write(a.subject + "," + a.name + "," + str(a.due_date) + "," + str(a.num_questions) + "\n")

#returns a list of assignments read from memory.txt
def readFromMemory():
    assignments = []
    with open('memory.txt', 'r') as f:
        for line in f:
            data = line.strip().split(',')
            assignments.append(Assignment(data[0], data[1], int(data[2]), int(data[3])))
    return assignments

#helper method for seeing if memory is empty
def is_file_empty(file_path):
    try:
        return os.path.getsize(file_path) == 0
    except FileNotFoundError:
        print(f"Error: The file '{file_path}' was not found.")
        return False

def generate_schedule(day_plans:list, workingDays:dict, today:int): #Orchestration method. need memory file to be complete here
    # goes based off of what is in memory.txt
    assignmentsREAD = readFromMemory()
    if len(assignmentsREAD) == 0:
        return -1

    for a in assignmentsREAD:
        count_freeDays = DaysFreeToWork(today, a.due_date, workingDays)
        allocateTasks(count_freeDays, a, day_plans)


#--------------------------#
#----------- MAIN ---------#
#--------------------------#
if __name__=="__main__":
    # SETTINGS
    day_plans = [Day('Saturday'), Day('Sunday'), Day('Monday'), Day('Tuesday'), Day('Wednesday'), Day('Thursday'), Day('Friday'), Day('Saturday'), Day('Sunday')]
    workingDays = {0:'y',           1:'n',          2:'y',          3:'y',          4:'y',          5:'y',          6:'y',          7:'y',          8:'y'}
    today = 1
    
    # 
    assignmentsTOTAL:list[Assignment] = []
    
    #HW: physics 53q, pc09, stathw 8 7q,         | STUDY: physics hw05(potE) 39q, stat hw6 11q
    # Gather INPUT FOR ASSIGNMENTS
    # For now, I will hardcode this, but eventually I will make it so that the user can input this data
    # 2 main cases for starting input: 1: theres nothing in memory, 2: already stuff, so we skip user input
    if is_file_empty('memory.txt'):
        print("No assignments in memory. Please input your assignments.")
        while True:
            subject = input("Enter subject (or 'done' to finish): ")
            if subject.lower() == 'done':
                break
            name = input("Enter assignment name: ")
            due_date = int(input("Enter due date (0-8): "))
            num_questions = int(input("Enter number of questions: "))
            assignmentsTOTAL.append(Assignment(subject, name, due_date, num_questions))

        writeToMemoryALL(assignmentsTOTAL) #we only need to do this if we added new assignments.

    # CALLS -> This happens AFTER all assignments added and written to memory.txt. Then, the program will read from memory.txt and call allocateTasks for each assignment.
    generate_schedule(day_plans, workingDays, today)
    
    for d in day_plans:
        print(d)
        print('\n')
