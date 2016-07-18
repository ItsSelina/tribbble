/*
 * Copyright 2016 Selina Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.selinali.tribbble.ui.deck;

import com.wenchao.cardstack.DefaultStackEventListener;

public abstract class DeckListener extends DefaultStackEventListener {

  int LEFT = 1, RIGHT = 2;

  DeckListener() {
    super(300);
  }

  abstract void onCardSwiped(int direction, int swipedIndex);

  @Override
  public void discarded(int index, int direction) {
    onCardSwiped(direction == 0 || direction == 2 ? LEFT : RIGHT, index - 1);
  }
}
