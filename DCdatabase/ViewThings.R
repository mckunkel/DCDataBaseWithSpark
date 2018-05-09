library(ggplot2)
library(data.table)
library(ggthemes)

df <- read.table("/Users/michaelkunkel/WORK/GIT_HUB/DCDataBaseWithSpark/DCdatabase/Run_3432.txt", header = FALSE)
head(df)
subdf<- subset(df,V4!=0)
pl<-ggplot(subdf,aes(x=V4))+geom_histogram()

