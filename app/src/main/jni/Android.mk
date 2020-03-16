# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

PROJ_PATH := $(call my-dir)
#include $(CLEAR_VARS)

include $(PROJ_PATH)/SDL2/Android.mk
include $(PROJ_PATH)/SDL2_image/Android.mk
include $(PROJ_PATH)/SDL2_mixer/Android.mk
include $(PROJ_PATH)/SDL2_net/Android.mk
include $(PROJ_PATH)/SDL2_ttf/Android.mk
include $(PROJ_PATH)/src/Android.mk
include $(PROJ_PATH)/demo/Android.mk
