package com.th3pl4gu3.locky.core.main

import com.google.firebase.database.Exclude

abstract class Credentials(
    @get:Exclude val id: String,
    @get:Exclude val user: String,
    @get:Exclude val name: String,
    @get:Exclude val additionalInfo: String?
)