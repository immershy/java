### addNode的主要步骤

1. 尝试获取锁cas设置

2. 获取失败, 构造node,加入tail

3. 如果prev为head节点, 自旋cas获取锁

### 共享锁的获取

1. setHeadAndPropagate 循环设置头节点并往下传播