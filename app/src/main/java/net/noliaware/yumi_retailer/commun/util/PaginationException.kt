package net.noliaware.yumi_retailer.commun.util

class PaginationException(val serviceError: ServiceError) : Exception(serviceError.toString())