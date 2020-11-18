package com.coroutines.ioTests.user.entrypoint

import com.coroutines.ioTests.user.document.User

data class UserRequest(
        val users: List<User>,
) {
}