package VSTU.ctQueue.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import VSTU.ctQueue.entity.AbstractEntity;

public class CrudImpl<Entity extends AbstractEntity> implements CrudService<Entity> {

    private JpaRepository<Entity, Long> repository;

    @Override
    public void create(final Entity entity) throws Exception {
        if (entity.getId() == null)
            repository.saveAndFlush(entity);
        else
            throw new Exception("New entity ID is not null");
    }

    @Override
    public Entity read(final long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    /**
     * ������ Entity �� {@link AbstractEntity#id} � ������ ������ ����� ������, ���
     * �������, ��� ����� ���� != null � ���������� �� ������.
     * 
     * @param entity ����������� �������� (����� �������������� ��
     *               {@link AbstractEntity#id}).
     */
    @Override
    public void update(final Entity entity) throws Exception {
        Entity beforeEntity = read(entity.getId());
        Class entityClass = entity.getClass();
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        while (entityClass != null) {
            for (Method method : entityClass.getMethods()) {
                if (isGetter(method))
                    getters.put(method.getName().substring(3), method);
                if (isSetter(method))
                    setters.put(method.getName().substring(3), method);
            }
            entityClass = entityClass.getSuperclass();
        }
        for (String key : getters.keySet()) {
            Object obj = getters.get(key).invoke(entity, null);
            if (null != obj && !String.valueOf(obj).isEmpty() && setters.containsKey(key))
                setters.get(key).invoke(beforeEntity, obj);
        }
        repository.saveAndFlush(beforeEntity);
    }

    @Override
    public void delete(final long id) throws Exception {
        repository.deleteById(id);
    }

    /**
     * ��������� ���� ���������
     * 
     * @return ������ {@link Entity}
     */
    public List<Entity> getAll() {
        return repository.findAll();
    }

    /**
     * ��������, �������� �� ����� "��������"
     * 
     * @param method �����
     * @return {@link Boolean}
     */
    private Boolean isGetter(Method method) {
        if (!method.getName().startsWith("get"))
            return Boolean.FALSE;
        if (method.getParameterTypes().length != 0)
            return Boolean.FALSE;
        if (void.class.equals(method.getReturnType()))
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    /**
     * ��������, �������� �� ����� "��������"
     * 
     * @param method �����
     * @return {@link Boolean}
     */
    private Boolean isSetter(Method method) {
        if (!method.getName().startsWith("set"))
            return Boolean.FALSE;
        if (method.getParameterTypes().length != 1)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public CrudImpl(final JpaRepository<Entity, Long> repository) {
        super();
        this.repository = repository;
    }
}
