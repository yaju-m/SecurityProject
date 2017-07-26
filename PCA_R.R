
#getting averages from each column 
getAverages <- function(raw_data_table) {
  col_names <- colnames(raw_data_table)
  mean_values <- list()
  i <- 1
  for (col in col_names) { 
    mean_values[[i]] <- mean(raw_data_table[[col_names[i]]])
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
    raw_data_table[[col_names[i]]] <- raw_data_table[[col_names[i]]] - current_mean
    i = i + 1
  }
  return(raw_data_table)
}

outputCovarianceTable <- function(subtracted_mean_table) { 
  col_names <- colnames(subtracted_mean_table)
  covariance_matrix = matrix(0L, length(col_names), length(col_names))
  i <- 1
  j <- 1
  #use col_names to make sure the iteration happens the correct number of times 
  for (col in col_names) { 
    for (col in col_names) { 
      curr_cov = cov(subtracted_mean_table[,i], subtracted_mean_table[,j])
      #set matrix value 
      covariance_matrix[i, j] <- curr_cov
      j <- j + 1
    }
    i <- i + 1
    j <- 1
  } 
  return(covariance_matrix)
}

getEigenVecs <- function(covariance_matrix) { 
  eigen_vals_vectors <- eigen(covariance_matrix)
  eigen_vectors <- eigen_vals_vectors$vectors
  return(eigen_vectors)
}

outputFinalData <- function(eigen_vecs, subtracted_data_table) {
  return(t(t(eigen_vecs) %*% t(data.matrix(subtracted_data_table))))
}

runPCA <- function(raw_data_table) { 
  subtracted_mean_data <- subtractAveragesFromVariables(raw_data_table, getAverages(raw_data_table))
  cov_matrix <- outputCovarianceTable(subtracted_mean_data)
  final_data <- outputFinalData(getEigenVecs(cov_matrix), subtracted_mean_data)
  return(final_data)
}

