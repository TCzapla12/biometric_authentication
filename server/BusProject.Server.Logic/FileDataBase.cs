namespace BusProject.Server.Logic
{
    using System;
    using System.Collections.Generic;
    using System.IO;
    using BusProject.Server.Model;
    public class FileDataBase : IFileDataBase
    {
        private static string FilesPath = "./../Files";

        private static List<ServerFile> Files = new List<ServerFile>();
        static FileDataBase()
        {
            Directory.CreateDirectory(FilesPath);
            DirectoryInfo di = new DirectoryInfo(@"./../Files");
            FileInfo[] fileInfo = di.GetFiles();
            foreach (FileInfo inf in fileInfo)
            {
                Files.Add(new ServerFile(inf.Name, inf.Length, inf.CreationTime));
            }
        }
        public FileDataBase()
        { }
        public List<ServerFile> GetFiles()
        {
            return Files;
        }
        public void AddFile (string id, double length, DateTime time)
        {
            Files.Add(new ServerFile(id, length, time));
        }
        public void DeleteFile(string id)
        {
            foreach(ServerFile file in Files)
            {
                if(file.Id == id)
                {
                    Files.Remove(file);
                    return;
                }
            }
        }
        public string GetPath()
        {
            return FilesPath;
        }
        public bool FileExist(string id)
        {
            foreach(ServerFile file in Files)
            {
                if(file.Id == id)
                {
                    return true;
                }
            }
            return false;
        }       
    }

    
}
