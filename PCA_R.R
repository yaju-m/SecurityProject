#Questions: 
#does the calling of the column using $ work?
#does the subtraction from a column work?

#getting averages from each column 
getAverages <- function(raw_data_table) {
  col_names <- colnames(raw_data_table)
  mean_values <- list()
  i <- 1
  for (col in col_names) { 
    mean_values[[i]] <- mean(raw_data_table$colnames[i])
    i <- i + 1
  }
  return(mean_values)
}

subtractAveragesFromVariables <- function(raw_data_table, mean_values) { 
  col_names <- colnames(raw_data_table)
  i <- 1
  for (mean in mean_values) { 
    #gets the current mean
    current_mean <- mean_values[[i]]
    #changes corresponding column all at once 
    raw_data_table$col_names[[i]] <- raw_data_table$col_names[[i]] - current_mean
    i = i + 1
  }
  return(raw_data_table)
}

