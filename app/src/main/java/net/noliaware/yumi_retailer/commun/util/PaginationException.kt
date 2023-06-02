package net.noliaware.yumi_retailer.commun.util

class PaginationException(val errorType: ErrorType) : Exception(errorType.toString())
