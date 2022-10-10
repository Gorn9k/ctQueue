package VSTU.ctQueue.service;

public interface CrudService<Entity> {

    /**
     * �������� ������ � ��.
     * 
     * @param entity �������� ��� ������
     * @throws Exception
     */
    void create(Entity entity) throws Exception;

    /**
     * ������ ������ �� ��.
     * 
     * @param id ���������� ������������� ��������
     * @return Entity
     */
    Entity read(long id);

    /**
     * ���������� ������ � ��.
     * 
     * @param entity �������� � ����������� �����������
     * @throws Exception
     */
    void update(Entity entity) throws Exception;

    /**
     * �������� ������ �� ��.
     * 
     * @param id ���������� ������������� ��������
     * @throws Exception
     */
    void delete(long id) throws Exception;
}