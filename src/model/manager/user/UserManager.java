package model.manager.user;

import model.user.User;
import model.user.manager.Admin;
import model.user.customer.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private final ObservableList<User> userList;
    private final Map<String, User> userMap;

    public UserManager() {
        userList = FXCollections.observableArrayList();
        userMap = new HashMap<>();
        loadInitialUsersInMemory();
    }

    /**
     * Thêm một người dùng mới vào hệ thống (trong bộ nhớ).
     *
     * @param user Người dùng cần thêm.
     * @param actingUser Người dùng đang thực hiện hành động.
     * @return true nếu thêm thành công, false nếu ID hoặc username đã tồn tại, hoặc người dùng không có quyền.
     */
    public boolean addUser(User user, User currentUser) {
        if (currentUser == null || !(currentUser instanceof Admin)) {
            System.out.println("Access Denied: User does not have permission to add users.");
            return false;
        }
        else {
            if (userMap.containsKey(user.getUsername())) {
                System.out.println("Error: User with username '" + user.getUsername() + "' already exists.");
                return false;
            }
            for (User u : userMap.values()) {
                if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                    System.out.println("Error: User with email '" + user.getEmail() + "' already exists.");
                    return false;
                }
                else if (u.getPhone().equalsIgnoreCase(user.getPhone())) {
                    System.out.println("Error: User with phone '" + user.getPhone() + "' already exists.");
                    return false;
                }
            }

            userList.add(user);
            userMap.put(user.getUsername(), user);
            System.out.println("User " + user.getUsername() + " added by " + currentUser.getUsername() + ".");
            return true;
        }
    }

    /**
     * Cập nhật thông tin của một người dùng đã tồn tại.
     *
     * @param updatedUser Đối tượng User với các thuộc tính đã được cập nhật.
     * @param actingUser Người dùng đang thực hiện hành động.
     * @return true nếu người dùng được cập nhật, false nếu không tìm thấy ID hoặc không có quyền.
     */
    public boolean updateUser(User updatedUser, User currentUser) {
        if (currentUser == null || !(currentUser instanceof Admin)) {
            System.out.println("Access Denied: User does not have permission to update users.");
            return false;
        }
        if (userMap.containsKey(updatedUser.getUsername())) {
            userMap.put(updatedUser.getUsername(), updatedUser);
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getUsername().equals(updatedUser.getUsername())) {
                    userList.set(i, updatedUser);
                    System.out.println("User " + updatedUser.getUsername() + " updated by " + currentUser.getUsername() + ".");
                    return true;
                }
            }
        }
        System.out.println("Error: User with username " + updatedUser.getUsername() + " not found for update.");
        return false;
    }

    /**
     * Xóa một người dùng khỏi hệ thống (chỉ trong bộ nhớ).
     *
     * @param userId ID của người dùng cần xóa.
     * @param actingUser Người dùng đang thực hiện hành động.
     * @return true nếu xóa thành công, false nếu không tìm thấy hoặc không có quyền.
     */
    public boolean deleteUser(User userToRemove, User currentUser) {
        if (currentUser == null || !(currentUser instanceof Admin)) {
            System.out.println("Access Denied: User does not have permission to delete users.");
            return false;
        }
        else {
            if (userToRemove == null) {
                System.out.println("Error: User with username " + userToRemove.getUsername() + " not found in memory.");
                return false;
            }
            if (userToRemove instanceof Admin && !(currentUser instanceof Admin)) { // Chỉ admin mới có thể xóa admin khác
                System.out.println("Error: User " + currentUser.getUsername() + " cannot delete another Admin (only another Admin can do this).");
                return false;
            }
            if (userToRemove.getUsername().equals(currentUser.getUsername())) {
                System.out.println("Error: User " + currentUser.getUsername() + " cannot delete self.");
                return false;
            }

            userList.remove(userToRemove);
            userMap.remove(userToRemove.getUsername());
            System.out.println("User " + userToRemove.getUsername() + " deleted by " + currentUser.getUsername() + ".");
            return true;
        }
    }

    /**
     * Lấy thông tin người dùng theo tên đăng nhập (username).
     *
     * @param username Tên đăng nhập của người dùng.
     * @return Đối tượng User nếu tìm thấy, null nếu không.
     */
    public User getUserByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
        
    }

    /**
     * Trả về danh sách tất cả người dùng.
     *
     * @return ObservableList<User> cho phép UI bind.
     */
    public ObservableList<User> getAllUsers(User currentUser) {
        if (currentUser == null || !(currentUser instanceof Admin)) {
            System.out.println("Access Denied: User does not have permission to view all users.");
            return null;
        }
        return userList;
    }

    /**
     * Đăng ký một tài khoản khách hàng mới (dành cho người dùng tự đăng ký).
     * Không yêu cầu quyền admin.
     *
     * @param username Tên đăng nhập.
     * @param password Mật khẩu (trong thực tế sẽ được hash).
     * @param email Địa chỉ email.
     * @param address Địa chỉ.
     * @return Đối tượng Customer mới nếu đăng ký thành công, null nếu username hoặc email đã tồn tại.
     */
    public Customer registerCustomer(String name, String email, String phone, String username, String password, String address) {
        // Kiểm tra username đã tồn tại chưa
        if (getUserByUsername(username) != null) {
            System.out.println("Registration failed: Username '" + username + "' already taken.");
            return null;
        }
        // Kiểm tra email đã tồn tại chưa 
        for (User u : userMap.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Registration failed: Email '" + email + "' is already registered.");
                return null;
            }
        }
        // Kiểm tra phone đã tồn tại chưa
        for (User u : userMap.values()) {
            if (u.getPhone().equalsIgnoreCase(phone)) {
                System.out.println("Registration failed: Phone '" + phone + "' is already registered.");
                return null;
            }
        }
        Customer newCustomer = new Customer(name, email, phone, username, password, address);
        userList.add(newCustomer);
        userMap.put(username, newCustomer);
        System.out.println("New customer '" + username + "' registered successfully.");
        return newCustomer;
    }

    /**
     * Xác thực thông tin đăng nhập của người dùng.
     *
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return Đối tượng User nếu xác thực thành công, null nếu thất bại.
     */
    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                System.out.println("Authentication successful for user: " + username);
                return user;
            }
        }
        System.out.println("Authentication failed for user: " + username + " (Invalid username or password).");
        return null;
    }

    private void loadInitialUsersInMemory() {
        System.out.println("UserManager: Loading initial admin...");

        // --- TẠO ADMIN ĐẦU TIÊN TẠI ĐÂY ---
        Admin adminUser = new Admin("Trung TT (PHD)", "trung.tt20239999", "0123456789", "admin", "12345678");
        userList.add(adminUser);
        userMap.put(adminUser.getUsername(), adminUser);
        System.out.println("UserManager: Initial Admin user created: " + adminUser.getName());
    }
}