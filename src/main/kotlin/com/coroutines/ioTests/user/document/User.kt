package com.coroutines.ioTests.user.document

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User (val name: String) {
}