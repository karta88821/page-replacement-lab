from csv import reader
import numpy as np
import matplotlib.pyplot as plt
from enum import Enum

class Title(Enum):
    PAGE_FAULTS = "Page Faults"
    INTERRUPTS = "Interrupts"
    DISK_WRITES = "Disk Writes"

class Pick(Enum):
    RANDOM = "RandomPick"
    LOCALITY = "LocalityPick"
    MYREF = "MyPick"

def showFigure(title, pick):
    start = -1
    end = -1

    plt.figure(figsize=(10,5), dpi=80)
    plt.grid(True, linestyle = "--", color = 'gray' , linewidth = '0.5', axis='y')

    match title:
        case Title.PAGE_FAULTS:
            match pick:
                case Pick.RANDOM:
                    start = 0;
                    end = 4;
                case Pick.LOCALITY:
                    start = 4;
                    end = 8;
                case Pick.MYREF:
                    start = 8;
                    end = 12;
        case Title.INTERRUPTS:
            match pick:
                case Pick.RANDOM:
                    start = 12;
                    end = 16;
                case Pick.LOCALITY:
                    start = 16;
                    end = 20;
                case Pick.MYREF:
                    start = 20;
                    end = 24;
        case Title.DISK_WRITES:
            match pick:
                case Pick.RANDOM:
                    start = 24;
                    end = 28;
                case Pick.LOCALITY:
                    start = 28;
                    end = 32;
                case Pick.MYREF:
                    start = 32;
                    end = 36;
    
    with open('pages.csv', 'r') as csv_file:
        csv_reader = reader(csv_file)
        list_of_rows = list(csv_reader)
        
        sizeOfFrames = ['30', '60', '90', '120', '150']
        x = np.arange(len(sizeOfFrames))
        width = 0.15

        for i in range(0, 36):
            list_of_rows[i].pop(0)

        for i in range(start, end):
            y = [int(j) for j in list_of_rows[i]] 
 
            match i % 4:
                case 0:
                    plt.bar(x, y, width, color = 'r', label = "FIFO_" + pick.value)
                case 1:
                    plt.bar(x + width, y, width, color = 'g', label = "Optimal_" + pick.value)
                case 2:
                    plt.bar(x + width * 2, y, width, color = 'b', label = "ARB_" + pick.value)
                case 3:
                    plt.bar(x + width * 3, y, width, color = 'y', label = "MyAlgo_" + pick.value)                
    
    plt.xticks(x, sizeOfFrames)
    plt.xlabel('Number of frames')
    plt.ylabel(title.value)
    plt.legend(bbox_to_anchor=(1, 0.9), loc='lower right')
    plt.title(pick.value)
    plt.show()    

showFigure(Title.DISK_WRITES, Pick.LOCALITY)